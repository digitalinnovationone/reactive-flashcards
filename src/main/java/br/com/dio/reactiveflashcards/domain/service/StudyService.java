package br.com.dio.reactiveflashcards.domain.service;

import br.com.dio.reactiveflashcards.domain.document.Card;
import br.com.dio.reactiveflashcards.domain.document.Question;
import br.com.dio.reactiveflashcards.domain.document.StudyCard;
import br.com.dio.reactiveflashcards.domain.document.StudyDocument;
import br.com.dio.reactiveflashcards.domain.dto.QuestionDTO;
import br.com.dio.reactiveflashcards.domain.dto.StudyDTO;
import br.com.dio.reactiveflashcards.domain.exception.DeckInStudyException;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.mapper.MailMapper;
import br.com.dio.reactiveflashcards.domain.mapper.StudyDomainMapper;
import br.com.dio.reactiveflashcards.domain.repository.StudyRepository;
import br.com.dio.reactiveflashcards.domain.service.query.DeckQueryService;
import br.com.dio.reactiveflashcards.domain.service.query.StudyQueryService;
import br.com.dio.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage.DECK_IN_STUDY;
import static br.com.dio.reactiveflashcards.domain.exception.BaseErrorMessage.STUDY_QUESTION_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {

    private final MailService mailService;
    private final UserQueryService userQueryService;
    private final DeckQueryService deckQueryService;
    private final StudyQueryService studyQueryService;
    private final StudyRepository studyRepository;
    private final StudyDomainMapper studyDomainMapper;
    private final MailMapper mailMapper;

    public Mono<StudyDocument> start(final StudyDocument document){
        return verifyStudy(document)
                .then(Mono.defer(() -> userQueryService.findById(document.userId())))
                .flatMap(user -> deckQueryService.findById(document.studyDeck().deckId()))
                .flatMap(deck -> fillDeckStudyCards(document, deck.cards()))
                .map(study -> study.toBuilder()
                        .question(studyDomainMapper.generateRandomQuestion(study.studyDeck().cards()))
                        .build())
                .doFirst(() -> log.info("==== generating a first random question"))
                .flatMap(studyRepository::save)
                .doOnSuccess(study -> log.info("a follow study was save {}", study));
    }

    private Mono<StudyDocument> fillDeckStudyCards(final StudyDocument document, final Set<Card> cards){
        return Flux.fromIterable(cards)
                .doFirst(() -> log.info("==== copy cards to new study"))
                .map(studyDomainMapper::toStudyCard)
                .collectList()
                .map(studyCards -> document.studyDeck().toBuilder().cards(Set.copyOf(studyCards)).build())
                .map(studyDeck -> document.toBuilder().studyDeck(studyDeck).build());
    }

    private Mono<Void> verifyStudy(final StudyDocument document){
        return studyQueryService.findPendingStudyByUserIdAndDeckId(document.userId(), document.studyDeck().deckId())
                .flatMap(study -> Mono.defer(() -> Mono.error(new DeckInStudyException(DECK_IN_STUDY
                        .params(document.userId(), document.studyDeck().deckId()).getMessage()))))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();
    }

    public Mono<StudyDocument> answer(final String id, final String answer){
        return studyQueryService.findById(id)
                .flatMap(studyQueryService::verifyIfFinished)
                .map(study -> studyDomainMapper.answer(study, answer))
                .zipWhen(this::getNextPossibilities)
                .map(tuple -> studyDomainMapper.toDTO(tuple.getT1(), tuple.getT2()))
                .flatMap(this::setNewQuestion)
                .map(studyDomainMapper::toDocument)
                .flatMap(studyRepository::save)
                .doFirst(() -> log.info("==== saving answer and next question if have one"));
    }

    private Mono<List<String>> getNextPossibilities(final StudyDocument document){
        return Flux.fromIterable(document.studyDeck().cards())
                .doFirst(() -> log.info("==== Getting question not used ow questions without right answers"))
                .map(StudyCard::front)
                .filter(asks -> document.questions().stream()
                        .filter(Question::isCorrect)
                        .map(Question::asked)
                        .noneMatch(q -> q.equals(asks)))
                .collectList()
                .flatMap(asks -> removeLastAsk(asks, document.getLastAnsweredQuestion().asked()));
    }

    private Mono<List<String>> removeLastAsk(final List<String> asks, final String asked) {
        return Mono.just(asks)
                .doFirst(() -> log.info("==== remove last asked question if it is not a last pending question in study"))
                .filter(a -> a.size() == 1)
                .switchIfEmpty(Mono.defer(() -> Mono.just(asks.stream()
                        .filter(a -> !a.equals(asked))
                        .collect(Collectors.toList()))));
    }

    private Mono<StudyDTO> setNewQuestion(final StudyDTO dto){
        return Mono.just(dto.hasAnyAnswer())
                .filter(BooleanUtils::isTrue)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException(STUDY_QUESTION_NOT_FOUND
                        .params(dto.id())
                        .getMessage()))))
                .flatMap(hasAnyAnswer -> generateNextQuestion(dto))
                .map(question -> dto.toBuilder().question(question).build())
                .onErrorResume(NotFoundException.class, e -> Mono.just(dto)
                        .onTerminateDetach()
                        .doOnSuccess(this::notifyUser));
    }

    private Mono<QuestionDTO> generateNextQuestion(final StudyDTO dto){
        return Mono.just(dto.remainAsks().get(new Random().nextInt(dto.remainAsks().size())))
                .doFirst(() -> log.info("==== select next random question"))
                .map(ask -> dto.studyDeck()
                        .cards()
                        .stream()
                        .filter(card -> card.front().equals(ask))
                        .map(studyDomainMapper::toQuestion)
                        .findFirst()
                        .orElseThrow());
    }

    private void notifyUser(final StudyDTO dto){
        userQueryService.findById(dto.userId())
                .zipWhen(user -> deckQueryService.findById(dto.studyDeck().deckId()))
                .map(tuple -> mailMapper.toDTO(dto, tuple.getT2(), tuple.getT1()))
                .flatMap(mailService::send)
                .subscribe();
    }

}
