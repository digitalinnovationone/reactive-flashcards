package br.com.dio.reactiveflashcards.domain.service.query;

import br.com.dio.reactiveflashcards.core.factorybot.document.DeckDocumentFactoryBot;
import br.com.dio.reactiveflashcards.core.factorybot.document.StudyDocumentFactoryBot;
import br.com.dio.reactiveflashcards.domain.exception.NotFoundException;
import br.com.dio.reactiveflashcards.domain.repository.StudyRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StudyQueryServiceTest {

    @Mock
    private StudyRepository studyRepository;

    private StudyQueryService studyQueryService;

    @BeforeEach
    void setup(){
        studyQueryService = new StudyQueryService(studyRepository);
    }

    @Test
    void findPendingStudyByUserIdAndDeckIdTest(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck).build();
        when(studyRepository.findByUserIdAndCompleteFalseAndStudyDeck_DeckId(anyString(), anyString()))
                .thenReturn(Mono.just(study));

        StepVerifier.create(studyQueryService.findPendingStudyByUserIdAndDeckId(study.userId(), study.studyDeck().deckId()))
                .assertNext(actual ->assertThat(actual).isNotNull())
                .verifyComplete();
        verify(studyRepository).findByUserIdAndCompleteFalseAndStudyDeck_DeckId(anyString(), anyString());
    }

    @Test
    void whenUserHasNonPendingStudyForDeckThenThrowError(){
        when(studyRepository.findByUserIdAndCompleteFalseAndStudyDeck_DeckId(anyString(), anyString())).thenReturn(Mono.empty());

        StepVerifier.create(studyQueryService.findPendingStudyByUserIdAndDeckId(ObjectId.get().toString(), ObjectId.get().toString()))
                .verifyError(NotFoundException.class);
        verify(studyRepository).findByUserIdAndCompleteFalseAndStudyDeck_DeckId(anyString(), anyString());
    }

    @Test
    void whenStudyNotFinishedThenReturnIt(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck).build();

        StepVerifier.create(studyQueryService.verifyIfFinished(study))
                .assertNext(actual -> assertThat(actual).isNotNull())
                .verifyComplete();
        verifyNoInteractions(studyRepository);
    }

    @Test
    void whenStudyFinishedThenThrowError(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck)
                .finishedStudy()
                .build();

        StepVerifier.create(studyQueryService.verifyIfFinished(study))
                .verifyError(NotFoundException.class);
        verifyNoInteractions(studyRepository);
    }

    @Test
    void getLastPendingQuestionTest(){
        var deck = DeckDocumentFactoryBot.builder().build();
        var study = StudyDocumentFactoryBot.builder(ObjectId.get().toString(), deck).build();
        when(studyRepository.findById(anyString())).thenReturn(Mono.just(study));

        StepVerifier.create(studyQueryService.getLastPendingQuestion(ObjectId.get().toString()))
                .assertNext(actual ->{
                    assertThat(actual.answered()).isNull();
                    assertThat(actual.answeredIn()).isNull();
                })
                .verifyComplete();
        verify(studyRepository).findById(anyString());
    }

    @Test
    void whenTryToGetPendingQuestionFromNonStoredStudyThenThrowError(){
        when(studyRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(studyQueryService.getLastPendingQuestion(ObjectId.get().toString()))
                        .verifyError(NotFoundException.class);
        verify(studyRepository).findById(anyString());
    }

}
