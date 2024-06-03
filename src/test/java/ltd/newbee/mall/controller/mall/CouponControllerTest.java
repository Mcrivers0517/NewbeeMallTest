package ltd.newbee.mall.controller.mall;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.controller.vo.NewBeeMallCouponVO;
import ltd.newbee.mall.controller.vo.NewBeeMallUserVO;
import ltd.newbee.mall.service.NewBeeMallCouponService;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CouponControllerTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private NewBeeMallCouponService newBeeMallCouponService;

    @InjectMocks
    private CouponController couponController;

    @Mock
    private PageResult<NewBeeMallCouponVO> mockPageResult;

    public static void assertResultEquals(Result expected, Result actual) {
        assertEquals(expected.getResultCode(), actual.getResultCode(), "Result code should be equal");
        assertEquals(expected.getMessage(), actual.getMessage(), "Message should be equal");
        assertEquals(expected.getData(), actual.getData(), "Data should be equal");
    }

    //CouponList

    //用户登录情况下
    //返回用户可获取优惠券
    @Test
    public void testCouponList_UserLoggedIn() {
        // Setup
        NewBeeMallUserVO userVO = new NewBeeMallUserVO();
        userVO.setUserId(1L);
        when(session.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(userVO);
        when(request.getSession()).thenReturn(session);

        NewBeeMallCouponVO coupon1 = new NewBeeMallCouponVO();
        NewBeeMallCouponVO coupon2 = new NewBeeMallCouponVO();
        List<NewBeeMallCouponVO> mockCoupons = Arrays.asList(coupon1, coupon2);
        when(newBeeMallCouponService.selectAvailableCoupon(1L)).thenReturn(mockCoupons);

        // Execute
        String viewName = couponController.couponList(request, session);

        // Verify
        assertEquals("mall/coupon-list", viewName);
        verify(request).setAttribute("coupons", mockCoupons);
    }

    //用户未登录情况
    //返回所有可获取优惠券
    @Test
    public void testCouponList_UserNotLoggedIn() {
        // Setup
        NewBeeMallUserVO userVO = new NewBeeMallUserVO();
        when(session.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(userVO);
        when(request.getSession()).thenReturn(session);

        NewBeeMallCouponVO coupon1 = new NewBeeMallCouponVO();
        NewBeeMallCouponVO coupon2 = new NewBeeMallCouponVO();
        List<NewBeeMallCouponVO> mockCoupons = Arrays.asList(coupon1, coupon2);
        when(newBeeMallCouponService.selectAvailableCoupon(null)).thenReturn(mockCoupons);

        // Execute
        String viewName = couponController.couponList(request, session);

        // Verify
        assertEquals("mall/coupon-list", viewName);
        verify(request).setAttribute("coupons", mockCoupons);
    }


    //MyCoupons

    //用户登录
    //返回用户所有优惠券
    @Test
    public void testMyCoupons_UserLoggedIn_AllParams() {
        // Setup
        NewBeeMallUserVO userVO = new NewBeeMallUserVO();
        userVO.setUserId(1L);
        when(session.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(userVO);

        Map<String, Object> params = new HashMap<>();
        params.put("status", "1");
        params.put("page", "2");
        params.put("limit", Constants.MY_COUPONS_LIMIT);

        PageQueryUtil pageUtil = new PageQueryUtil(params);
        when(newBeeMallCouponService.selectMyCoupons(any(PageQueryUtil.class))).thenReturn(mockPageResult);

        // Execute
        String viewName = couponController.myCoupons(params, request, session);

        // Verify
        assertEquals("mall/my-coupons", viewName);
        verify(request).setAttribute("pageResult", mockPageResult);
        verify(request).setAttribute("path", "myCoupons");
        verify(request).setAttribute("status", 1);
    }


    //用户未登录
    //空指针异常
    @Test
    public void testMyCoupons_UserNotLoggedIn() {
        // Setup
        when(session.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(null);

        // Execute & Verify
        assertThrows(NullPointerException.class, () -> {
            couponController.myCoupons(new HashMap<>(), request, session);
        });
    }


    //save

    //成功保存优惠券
    //
    @Test
    public void testSave_CouponSavedSuccessfully() {
        // Setup
        Long couponId = 1L;
        Long userId = 1L;
        NewBeeMallUserVO userVO = new NewBeeMallUserVO();
        userVO.setUserId(userId);

        when(session.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(userVO);
        when(newBeeMallCouponService.saveCouponUser(couponId, userId)).thenReturn(true);

        // Execute
        Result result = couponController.save(couponId, session);

        // Verify
        assertNotNull(result);
        assertResultEquals(ResultGenerator.genDmlResult(true), result);
        verify(newBeeMallCouponService).saveCouponUser(couponId, userId);
    }


    //保存优惠券失败
    //
    @Test
    public void testSave_CouponSaveFailed() {
        // Setup
        Long couponId = 1L;
        Long userId = 1L;
        NewBeeMallUserVO userVO = new NewBeeMallUserVO();
        userVO.setUserId(userId);

        when(session.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(userVO);
        when(newBeeMallCouponService.saveCouponUser(couponId, userId)).thenReturn(false);

        // Execute
        Result result = couponController.save(couponId, session);

        // Verify
        assertNotNull(result);
        assertResultEquals(ResultGenerator.genDmlResult(false), result);
        verify(newBeeMallCouponService).saveCouponUser(couponId, userId);
    }


    //用户未登录
    //
    @Test
    public void testSave_UserNotLoggedIn() {
        // Setup
        Long couponId = 1L;

        when(session.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(null);

        // Execute & Verify
        assertThrows(NullPointerException.class, () -> {
            couponController.save(couponId, session);
        });
    }




    //delete

    @Test
    public void testDelete_CouponUserDeletedSuccessfully() {
        // Setup
        Long couponUserId = 1L;

        when(newBeeMallCouponService.deleteCouponUser(couponUserId)).thenReturn(true);

        // Execute
        Result result = couponController.delete(couponUserId);

        // Verify
        Result expected = ResultGenerator.genDmlResult(true);
        assertResultEquals(expected, result);
        verify(newBeeMallCouponService).deleteCouponUser(couponUserId);
    }

    @Test
    public void testDelete_CouponUserDeletionFailed() {
        // Setup
        Long couponUserId = 1L;

        when(newBeeMallCouponService.deleteCouponUser(couponUserId)).thenReturn(false);

        // Execute
        Result result = couponController.delete(couponUserId);

        // Verify
        Result expected = ResultGenerator.genDmlResult(false);
        assertResultEquals(expected, result);
        verify(newBeeMallCouponService).deleteCouponUser(couponUserId);
    }

}