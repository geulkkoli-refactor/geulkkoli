package com.geulkkoli.web.post.dto;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class PostEditRequestDTOTest {
    @Test
    void testCanEqual() {
        String html = "<div class=\"tt_article_useless_p_margin contents_style\" style=\"-webkit-text-stroke-width:0px;color:rgb(51, 51, 51);font-family:&quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif;font-size:16px;font-style:normal;font-variant-caps:normal;font-variant-ligatures:normal;font-weight:400;letter-spacing:normal;orphans:2;text-align:left;text-decoration-color:initial;text-decoration-style:initial;text-decoration-thickness:initial;text-indent:0px;text-transform:none;white-space:normal;widows:2;word-break:break-word;word-spacing:0px;\"><h4 style=\"color:rgba(0, 0, 0, 0.87);font-size:18px;font-weight:normal;line-height:1.33;margin:24px 0px;padding:0px;\" data-ke-size=\"size20\">런던파의 단위 테스트 접근방식의 이점과 그에 대한 반박</h4><ul><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:disc;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">테스트가 세밀해서 한 번에 한 클래스만 확인한다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:disc;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">서로 연결된 클래스의 그래프가 커져도 모든 협력자가 테스트 대역으로 대체되기 때문에 테스트 작성시 걱정할 필요가 없다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:disc;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">테스트가 실패하면 어떤 기능이 실패했는지 확실히 알 수 있다.</li></ul><hr><p style=\"margin:0px 0px 20px;padding:0px !important 0px;\" data-ke-size=\"size16\">&nbsp;저자는 이 같은 이점에는 문제가 있다고 다음 과 같이 말한다.</p><ul><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:circle;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">테스트는 코드 단위가 아니라 동작 단위를 검증해야 한다. 문제 영역에 의미가 있는, 이상적으로는 비즈니즈 담당자가 유용하다고 인식할 수 있는 것을 검증해야한다. 이러한 동작에는 여러 클래스가 걸쳐 있을 수 도 있고 작은 메서드만 걸쳐 있을 수 도 있다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:circle;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">&nbsp;동작 보다 작은 걸 테스트의 목표로 하다 보면 무엇을 검증하는 지 정확히 이해하기 힘들어 질 수 있다. 가령 공을 던진다는 동작이 공을 집는다 -&gt; 공을 집은 손가락의 위치를 어디쯤으로 해라 ~-&gt; ... -&gt; 공을 손에 놓는다 와 같이 내부적인 세부 구현을 노출 시키는 응집도가 떨어지는 코드를 짤 위험이 생긴다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:circle;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">코드 그래프가 단위 테스트에서 몹시 커졌다면 이는 코드 설계의 문제의 결과다. 테스트에서 이 문제를 지적하는 건 좋은 일이다. 이를 좋은 부정 지표라고 한다. 높은 정확도로 저품질을 예측하기 때문이다. 목은 이런 문제를 감출 뿐이다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:circle;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">테스트에 어떤 버그가 있는 판단하는 데 도움이 되지만, 어떤 변경 이후 테스트를 매번 확인했다면 늘 마지막에 수정한 것이 버그의 원일 것이기에 이점이 그리 크지 않다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:circle;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">런던파 테스트의 가장 큰 문제는 과잉 명세 테스트 대상 세부 구현에 결합된 테스트 문제다. 이는 리팩토링 내성을 크게 저하 시킨다.</li></ul><h4 style=\"color:rgba(0, 0, 0, 0.87);font-size:18px;font-weight:normal;line-height:1.33;margin:24px 0px;padding:0px;\" data-ke-size=\"size20\">통합 테스트</h4><ul><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:disc;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">통합 테스트는 단위 테스트 기준 중 하나 이상을 충족하지 못하는 테스트다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:disc;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">엔드 투 엔드 테스트는 통합 테스트의 일부다. 최종 사용자 관점에서 시스템을 검증한다.&nbsp;</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:disc;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">통합 테스트는 외부 의존성을 한두 개만 갖고 작동한다.</li><li style=\"color:rgba(0, 0, 0, 0.87);float:initial;font-size:16px;height:initial;line-height:24px;list-style-image:unset;list-style-position:unset;list-style-type:disc;margin:0px;overflow:initial;padding:0px;width:initial;word-break:break-all;\">엔트 두&nbsp; 엔드 테스트는 애플리케이션과 함께 작동하는 프로세스 외부 의존성의 전부 또는 대부분에 직접 접근한다.</li></ul><p style=\"margin:0px 0px 20px;padding:0px !important 0px;\">&nbsp;</p></div><p><span style=\"cursor:pointer;\" data-url=\"https://blog.kakaocdn.net/dn/Yy0UW/btspOCVOpa4/khzPyskrquDrdPtjkaaBL1/img.png\" data-lightbox=\"lightbox\"><img class=\"image_resized\" style=\"border-width:0px;display:inline-block;height:auto;margin:0px;max-width:100%;width:800px;\" src=\"https://blog.kakaocdn.net/dn/Yy0UW/btspOCVOpa4/khzPyskrquDrdPtjkaaBL1/img.png\" srcset=\"https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&amp;fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FYy0UW%2FbtspOCVOpa4%2FkhzPyskrquDrdPtjkaaBL1%2Fimg.png\" sizes=\"100vw\" onerror=\"this.onerror=null; this.src='//t1.daumcdn.net/tistory_admin/static/images/no-image-v1.png'; this.srcset='//t1.daumcdn.net/tistory_admin/static/images/no-image-v1.png';\" data-origin-width=\"3012\" data-origin-height=\"1478\"></span></p><div class=\"container_postbtn #post_button_group\" style=\"-webkit-text-stroke-width:0px;border-bottom:1px solid rgb(238, 238, 238);clear:both;color:rgb(51, 51, 51);font-family:&quot;Apple SD Gothic Neo&quot;, &quot;Malgun Gothic&quot;, &quot;맑은 고딕&quot;, Dotum, 돋움, &quot;Noto Sans KR&quot;, &quot;Nanum Gothic&quot;, Lato, Helvetica, sans-serif;font-size:16px;font-style:normal;font-variant-caps:normal;font-variant-ligatures:normal;font-weight:400;letter-spacing:normal;orphans:2;padding:35px 0px;position:relative;text-align:left;text-decoration-color:initial;text-decoration-style:initial;text-decoration-thickness:initial;text-indent:0px;text-transform:none;white-space:normal;widows:2;word-spacing:0px;\"><div class=\"postbtn_like\" style=\"border-radius:16px;border:1px solid rgba(185, 185, 185, 0.5);float:left;font-size:0px !important;letter-spacing:normal;margin:0px;padding:0px 9px;\"><div class=\"wrap_btn\" style=\"float:left;letter-spacing:normal;margin:0px;padding:0px;position:relative;\" id=\"reaction-15\">&nbsp;</div><div class=\"wrap_btn wrap_btn_share\" style=\"float:left;letter-spacing:normal;margin:0px;padding:0px;position:relative;\">&nbsp;</div><div class=\"wrap_btn\" style=\"float:left;letter-spacing:normal;margin:0px;padding:0px;position:relative;\">&nbsp;</div><div class=\"wrap_btn wrap_btn_etc\" style=\"float:left;letter-spacing:normal;margin:0px;padding:0px;position:relative;\" data-entry-id=\"15\" data-entry-visibility=\"public\" data-category-visibility=\"public\">&nbsp;</div></div></div>";
        Document parse = Jsoup.parse(html);

        log.info("{}", parse.html());
    }

}