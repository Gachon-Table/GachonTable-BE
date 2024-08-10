package site.gachontable.gachontablebe.domain.waiting.usecase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import site.gachontable.gachontablebe.domain.pub.domain.Pub;
import site.gachontable.gachontablebe.domain.pub.domain.repository.PubRepository;
import site.gachontable.gachontablebe.domain.pub.exception.PubNotFoundException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
class CreateWaitingImplTest {

    @Autowired
    private PubRepository pubRepository;

    @Autowired
    private CreateWaitingImpl createWaitingImpl;

    @Test
    public void executeForTest() throws InterruptedException {
        Pub pub = pubRepository.findByPubId(1).orElseThrow(PubNotFoundException::new);
        int initialWaitingCount = pub.getWaitingCount();

        int numberOfThreads = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            int tel = i;
            executorService.submit(() -> {
                try {
                    createWaitingImpl.executeWithRedisson(tel, "테스트");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Pub updatedPub = pubRepository.findByPubId(1).orElseThrow(PubNotFoundException::new);
        int finalWaitingCount = updatedPub.getWaitingCount();

        assertThat(finalWaitingCount).isEqualTo(initialWaitingCount + numberOfThreads);
    }
}