package ltd.newbee.mall.controller.mall;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.controller.vo.NewBeeMallUserVO;
import ltd.newbee.mall.entity.NewBeeMallShoppingCartItem;
import ltd.newbee.mall.exception.NewBeeMallException;
import ltd.newbee.mall.service.NewBeeMallCouponService;
import ltd.newbee.mall.service.NewBeeMallShoppingCartService;
import ltd.newbee.mall.util.Result;
import ltd.newbee.mall.util.ResultGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ShoppingCartControllerTest {
    @Mock
    private HttpSession httpSession;

    @Mock
    private HttpServletRequest request;

    @Mock
    private NewBeeMallShoppingCartService newBeeMallShoppingCartService;

    @Mock
    private NewBeeMallCouponService newBeeMallCouponService;

    @InjectMocks
    private ShoppingCartController cartController;

    public static void assertResultEquals(Result expected, Result actual) {
        assertEquals(expected.getResultCode(), actual.getResultCode(), "Result code should be equal");
        assertEquals(expected.getMessage(), actual.getMessage(), "Message should be equal");
        assertEquals(expected.getData(), actual.getData(), "Data should be equal");
    }


    //cartListPage

    //用户已登录且购物车有商品
    @Test
    public void testCartListPage_UserLoggedInWithItems() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = new ArrayList<>();
        NewBeeMallShoppingCartItemVO item = new NewBeeMallShoppingCartItemVO();
        item.setGoodsCount(2);
        item.setSellingPrice(50);
        myShoppingCartItems.add(item);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.getMyShoppingCartItems(anyLong())).thenReturn(myShoppingCartItems);

        // Execute
        String viewName = cartController.cartListPage(request, httpSession);

        // Verify
        assertEquals("mall/cart", viewName);
        verify(request).setAttribute("itemsTotal", 2);
        verify(request).setAttribute("priceTotal", 100);
        verify(request).setAttribute("myShoppingCartItems", myShoppingCartItems);
    }


    //用户已登录但购物车为空
    @Test
    public void testCartListPage_UserLoggedInWithEmptyCart() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.getMyShoppingCartItems(anyLong())).thenReturn(new ArrayList<>());

        // Execute
        String viewName = cartController.cartListPage(request, httpSession);

        // Verify
        assertEquals("mall/cart", viewName);
        verify(request).setAttribute("itemsTotal", 0);
        verify(request).setAttribute("priceTotal", 0);
        verify(request).setAttribute("myShoppingCartItems", new ArrayList<>());
    }


    //测试购物项总数小于1
    @Test
    public void testCartListPage_ItemsTotalLessThanOne() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = new ArrayList<>();
        NewBeeMallShoppingCartItemVO item = new NewBeeMallShoppingCartItemVO();
        item.setGoodsCount(0);
        item.setSellingPrice(50);
        myShoppingCartItems.add(item);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.getMyShoppingCartItems(anyLong())).thenReturn(myShoppingCartItems);

        // Execute & Verify
        NewBeeMallException exception = assertThrows(NewBeeMallException.class, () -> {
            cartController.cartListPage(request, httpSession);
        });

        assertEquals("购物项不能为空", exception.getMessage());
    }


    //购物项总价格小于1
    @Test
    public void testCartListPage_PriceTotalLessThanOne() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = new ArrayList<>();
        NewBeeMallShoppingCartItemVO item = new NewBeeMallShoppingCartItemVO();
        item.setGoodsCount(1);
        item.setSellingPrice(0);
        myShoppingCartItems.add(item);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.getMyShoppingCartItems(anyLong())).thenReturn(myShoppingCartItems);

        // Execute & Verify
        NewBeeMallException exception = assertThrows(NewBeeMallException.class, () -> {
            cartController.cartListPage(request, httpSession);
        });

        assertEquals("购物项价格异常", exception.getMessage());
    }







    //saveNewBeeMallShoppingCartItem

    //成功添加购物车商品
    @Test
    public void testSaveNewBeeMallShoppingCartItem_Success() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = new NewBeeMallShoppingCartItem();
        newBeeMallShoppingCartItem.setGoodsId(100L);
        newBeeMallShoppingCartItem.setGoodsCount(1);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.saveNewBeeMallCartItem(any(NewBeeMallShoppingCartItem.class))).thenReturn(ServiceResultEnum.SUCCESS.getResult());

        // Execute
        Result result = cartController.saveNewBeeMallShoppingCartItem(newBeeMallShoppingCartItem, httpSession);

        // Verify
        Result expected = ResultGenerator.genSuccessResult();
        assertResultEquals(expected, result);
        verify(newBeeMallShoppingCartService).saveNewBeeMallCartItem(newBeeMallShoppingCartItem);
    }

    //添加购物车商品失败
    @Test
    public void testSaveNewBeeMallShoppingCartItem_Failure() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = new NewBeeMallShoppingCartItem();
        newBeeMallShoppingCartItem.setGoodsId(100L);
        newBeeMallShoppingCartItem.setGoodsCount(1);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.saveNewBeeMallCartItem(any(NewBeeMallShoppingCartItem.class))).thenReturn(ServiceResultEnum.ERROR.getResult());

        // Execute
        Result result = cartController.saveNewBeeMallShoppingCartItem(newBeeMallShoppingCartItem, httpSession);

        // Verify
        Result expected = ResultGenerator.genFailResult(ServiceResultEnum.ERROR.getResult());
        assertResultEquals(expected, result);
        verify(newBeeMallShoppingCartService).saveNewBeeMallCartItem(newBeeMallShoppingCartItem);
    }





    //updateNewBeeMallShoppingCartItem(输入购物车项)

    //成功更新购物车商品路径
    @Test
    public void testUpdateNewBeeMallShoppingCartItem_Success() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = new NewBeeMallShoppingCartItem();
        newBeeMallShoppingCartItem.setGoodsId(100L);
        newBeeMallShoppingCartItem.setGoodsCount(1);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.updateNewBeeMallCartItem(any(NewBeeMallShoppingCartItem.class))).thenReturn(ServiceResultEnum.SUCCESS.getResult());

        // Execute
        Result result = cartController.updateNewBeeMallShoppingCartItem(newBeeMallShoppingCartItem, httpSession);

        // Verify
        Result expected = ResultGenerator.genSuccessResult();
        assertResultEquals(expected, result);
        verify(newBeeMallShoppingCartService).updateNewBeeMallCartItem(newBeeMallShoppingCartItem);
    }

    //更新购物车商品失败路径
    @Test
    public void testUpdateNewBeeMallShoppingCartItem_Failure() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = new NewBeeMallShoppingCartItem();
        newBeeMallShoppingCartItem.setGoodsId(100L);
        newBeeMallShoppingCartItem.setGoodsCount(1);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.updateNewBeeMallCartItem(any(NewBeeMallShoppingCartItem.class))).thenReturn(ServiceResultEnum.ERROR.getResult());

        // Execute
        Result result = cartController.updateNewBeeMallShoppingCartItem(newBeeMallShoppingCartItem, httpSession);

        // Verify
        Result expected = ResultGenerator.genFailResult(ServiceResultEnum.ERROR.getResult());
        assertResultEquals(expected, result);
        verify(newBeeMallShoppingCartService).updateNewBeeMallCartItem(newBeeMallShoppingCartItem);
    }




    //updateNewBeeMallShoppingCartItem(输入ID)

    //成功更新购物车商品路径
    @Test
    public void testUpdateNewBeeMallShoppingCartItem_DeleteSuccess() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.deleteById(1L, user.getUserId())).thenReturn(true);

        // Execute
        Result result = cartController.updateNewBeeMallShoppingCartItem(1L, httpSession);

        // Verify
        Result expected = ResultGenerator.genSuccessResult();
        assertResultEquals(expected, result);
        verify(newBeeMallShoppingCartService).deleteById(1L, user.getUserId());
    }


    //更新购物车商品失败路径
    @Test
    public void testUpdateNewBeeMallShoppingCartItem_DeleteFailure() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.deleteById(1L, user.getUserId())).thenReturn(false);

        // Execute
        Result result = cartController.updateNewBeeMallShoppingCartItem(1L, httpSession);

        // Verify
        Result expected = ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
        assertResultEquals(expected, result);
        verify(newBeeMallShoppingCartService).deleteById(1L, user.getUserId());
    }







    //settlePage


    //用户已登录且购物车中有商品
    @Test
    public void testSettlePage_WithItems() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        NewBeeMallShoppingCartItemVO item1 = new NewBeeMallShoppingCartItemVO();
        item1.setGoodsCount(2);
        item1.setSellingPrice(100);
        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = List.of(item1);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.getMyShoppingCartItems(user.getUserId())).thenReturn(myShoppingCartItems);
        when(newBeeMallCouponService.selectOrderCanUseCoupons(anyList(), anyInt(), anyLong())).thenReturn(Collections.emptyList());

        // Execute
        String viewName = cartController.settlePage(request, httpSession);

        // Verify
        assertEquals("mall/order-settle", viewName);
        verify(request).setAttribute("priceTotal", 200);
        verify(request).setAttribute("myShoppingCartItems", myShoppingCartItems);
        verify(request).setAttribute("coupons", Collections.emptyList());
    }


    //用户已登录且购物车中无商品
    @Test
    public void testSettlePage_WithoutItems() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.getMyShoppingCartItems(user.getUserId())).thenReturn(Collections.emptyList());

        // Execute
        String viewName = cartController.settlePage(request, httpSession);

        // Verify
        assertEquals("/shop-cart", viewName);
    }


    //用户已登录但购物车商品价格异常
    @Test
    public void testSettlePage_PriceException() {
        // Setup
        NewBeeMallUserVO user = new NewBeeMallUserVO();
        user.setUserId(1L);

        NewBeeMallShoppingCartItemVO item1 = new NewBeeMallShoppingCartItemVO();
        item1.setGoodsCount(1);
        item1.setSellingPrice(-100);
        List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = List.of(item1);

        when(httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY)).thenReturn(user);
        when(newBeeMallShoppingCartService.getMyShoppingCartItems(user.getUserId())).thenReturn(myShoppingCartItems);

        // Execute & Verify
        Exception exception = assertThrows(NewBeeMallException.class, () -> {
            cartController.settlePage(request, httpSession);
        });

        assertEquals("购物项价格异常", exception.getMessage());
    }




}