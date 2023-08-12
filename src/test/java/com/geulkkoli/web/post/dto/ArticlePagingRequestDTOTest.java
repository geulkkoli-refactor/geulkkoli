package com.geulkkoli.web.post.dto;

import com.geulkkoli.domain.post.Post;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class ArticlePagingRequestDTOTest {

    @DisplayName("게시글 요약본을 11자 보여 준다.")
    @Test
    void getContentSummary() {

        String content = "런던파의 단위 테스트 접근방식의 이점과 그에 대한 반박\n" +
                "\n" +
                "테스트가 세밀해서 한 번에 한 클래스만 확인한다.\n" +
                "서로 연결된 클래스의 그래프가 커져도 모든 협력자가 테스트 대역으로 대체되기 때문에 테스트 작성시 걱정할 필요가 없다.\n" +
                "테스트가 실패하면 어떤 기능이 실패했는지 확실히 알 수 있다.\n" +
                "\n" +
                "\n" +
                "\n" +
                " 저자는 이 같은 이점에는 문제가 있다고 다음 과 같이 말한다.\n" +
                "\n" +
                "테스트는 코드 단위가 아니라 동작 단위를 검증해야 한다. 문제 영역에 의미가 있는, 이상적으로는 비즈니즈 담당자가 유용하다고 인식할 수 있는 것을 검증해야한다. 이러한 동작에는 여러 클래스가 걸쳐 있을 수 도 있고 작은 메서드만 걸쳐 있을 수 도 있다.\n" +
                " 동작 보다 작은 걸 테스트의 목표로 하다 보면 무엇을 검증하는 지 정확히 이해하기 힘들어 질 수 있다. 가령 공을 던진다는 동작이 공을 집는다 -> 공을 집은 손가락의 위치를 어디쯤으로 해라 ~-> ... -> 공을 손에 놓는다 와 같이 내부적인 세부 구현을 노출 시키는 응집도가 떨어지는 코드를 짤 위험이 생긴다.\n" +
                "코드 그래프가 단위 테스트에서 몹시 커졌다면 이는 코드 설계의 문제의 결과다. 테스트에서 이 문제를 지적하는 건 좋은 일이다. 이를 좋은 부정 지표라고 한다. 높은 정확도로 저품질을 예측하기 때문이다. 목은 이런 문제를 감출 뿐이다.\n" +
                "테스트에 어떤 버그가 있는 판단하는 데 도움이 되지만, 어떤 변경 이후 테스트를 매번 확인했다면 늘 마지막에 수정한 것이 버그의 원일 것이기에 이점이 그리 크지 않다.\n" +
                "런던파 테스트의 가장 큰 문제는 과잉 명세 테스트 대상 세부 구현에 결합된 테스트 문제다. 이는 리팩토링 내성을 크게 저하 시킨다.\n" +
                "\n" +
                "통합 테스트\n" +
                "\n" +
                "통합 테스트는 단위 테스트 기준 중 하나 이상을 충족하지 못하는 테스트다.\n" +
                "엔드 투 엔드 테스트는 통합 테스트의 일부다. 최종 사용자 관점에서 시스템을 검증한다. \n" +
                "통합 테스트는 외부 의존성을 한두 개만 갖고 작동한다.\n" +
                "엔트  엔드 테스트는 애플리케이션과 함께 작동하는 프로세스 외부 의존성의 전부 또는 대부분에 직접 접근한다";

        Document parse = Jsoup.parse(content);
        log.info("parse = {}", parse.html());
        String s = parse.html().replaceAll("<[^>]*>", "").replaceAll("[^\\w\\s]+[A-Za-z]+[^\\w\\s]+", "");
        log.info("s = {}", s);
    }
}