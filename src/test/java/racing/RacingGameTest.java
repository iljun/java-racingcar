package racing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import racing.domain.RacingGame;
import racing.domain.RandomMovement;
import racing.dto.RacingCreateValueObject;
import racing.dto.RacingGameResult;

import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RacingGameTest {

    @ParameterizedTest
    @MethodSource("provideNotValidTotalRacingGameCount")
    @DisplayName("RacingGame racing 횟수 유효성 검사")
    void validateTotalRacingGameCount(RacingCreateValueObject racingCreateValueObject) {
        this.createRacingGameExceptionTest(racingCreateValueObject);
    }

    @ParameterizedTest
    @MethodSource("provideNotValidCarCount")
    @DisplayName("RacingGame 자동차 대수 유효성 검사")
    void validationTest(RacingCreateValueObject racingCreateValueObject) {
        this.createRacingGameExceptionTest(racingCreateValueObject);
    }

    private void createRacingGameExceptionTest(RacingCreateValueObject racingCreateValueObject) {
        assertThatThrownBy(() -> this.createRacingGame(racingCreateValueObject))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private RacingGame createRacingGame(RacingCreateValueObject racingCreateValueObject) {
        return new RacingGame(racingCreateValueObject, new RandomMovement(new Random()));
    }

    @ParameterizedTest
    @MethodSource("provideRacingGameArguments")
    @DisplayName("RacingGame StartRacing 테스트")
    void startRacing(RacingCreateValueObject racingCreateValueObject) {
        RacingGame racingGame = this.createRacingGame(racingCreateValueObject);
        racingGame.executeRacing();
        RacingGameResult racingGameResult = new RacingGameResult(racingGame);
        assertThat(racingGameResult.getRacingResult()).hasSize(racingCreateValueObject.carCount());
    }

    private static Stream<Arguments> provideNotValidCarCount() {
        return Stream.of(
                Arguments.of(new RacingCreateValueObject(1, 0)),
                Arguments.of(new RacingCreateValueObject(1, -1))
        );
    }

    private static Stream<Arguments> provideNotValidTotalRacingGameCount() {
        return Stream.of(
                Arguments.of(new RacingCreateValueObject(0, 1)),
                Arguments.of(new RacingCreateValueObject(-1, 1))
        );
    }
    private static Stream<Arguments> provideRacingGameArguments() {
        return Stream.of(
                Arguments.of(new RacingCreateValueObject(1, 1)),
                Arguments.of(new RacingCreateValueObject(3, 10)),
                Arguments.of(new RacingCreateValueObject(5, 1))
        );
    }
}