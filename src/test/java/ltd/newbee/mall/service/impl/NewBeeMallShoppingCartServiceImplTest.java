package ltd.newbee.mall.service.impl;

import ltd.newbee.mall.common.Constants;
import ltd.newbee.mall.common.ServiceResultEnum;
import ltd.newbee.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.dao.NewBeeMallShoppingCartItemMapper;
import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.entity.NewBeeMallShoppingCartItem;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class NewBeeMallShoppingCartServiceImplTest {


    /* 白盒测试 */
    @Nested
    class WhiteBoxTests{
        @InjectMocks
        private NewBeeMallShoppingCartServiceImpl newBeeMallShoppingCartService;

        @Mock
        private NewBeeMallShoppingCartItemMapper newBeeMallShoppingCartItemMapper;

        @Mock
        private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

        /*
        saveNewBeeMallCartItem
         */

        /* WB1 商品已存在 */
        @Test
        public void testSaveNewBeeMallCartItem_CartItemExists() {
            // 准备测试数据
            NewBeeMallShoppingCartItem existingCartItem = new NewBeeMallShoppingCartItem();
            existingCartItem.setCartItemId(158L);
            existingCartItem.setUserId(17L);
            existingCartItem.setGoodsId(10147L);
            existingCartItem.setGoodsCount(1);
            existingCartItem.setIsDeleted((byte)0);

            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(3);
            newCartItem.setIsDeleted((byte)0);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(17L, 10147L)).thenReturn(existingCartItem);
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(158L)).thenReturn(existingCartItem);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(17L, 10147L);
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(158L);
        }

        /* WB2 商品不存在 */
        @Test
        public void testSaveNewBeeMallCartItem_GoodNotExist() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(1L);
            newCartItem.setGoodsId(1L);
            newCartItem.setGoodsCount(2);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(1L, 1L)).thenReturn(null);
            when(newBeeMallGoodsMapper.selectByPrimaryKey(1L)).thenReturn(null);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.GOODS_NOT_EXIST.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(1L, 1L);
            verify(newBeeMallGoodsMapper).selectByPrimaryKey(1L);
        }

        /* WB3 超出单个商品的最大数量 */
        @Test
        public void testSaveNewBeeMallCartItem_ExceedItemLimit() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER + 1);

            NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
            newBeeMallGoods.setGoodsId(10147L);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(17L, 10147L)).thenReturn(null);
            when(newBeeMallGoodsMapper.selectByPrimaryKey(10147L)).thenReturn(newBeeMallGoods);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(17L,10147L);
            verify(newBeeMallGoodsMapper).selectByPrimaryKey(10147L);
        }

        /* WB4 超出购物车总数量限制 */
        @Test
        public void testSaveNewBeeMallCartItem_ExceedTotalLimit() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(1);

            NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
            newBeeMallGoods.setGoodsId(10147L);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(17L, 10147L)).thenReturn(null);
            when(newBeeMallGoodsMapper.selectByPrimaryKey(10147L)).thenReturn(newBeeMallGoods);
            when(newBeeMallShoppingCartItemMapper.selectCountByUserId(17L)).thenReturn(Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(17L,10147L);
            verify(newBeeMallGoodsMapper).selectByPrimaryKey(10147L);
            verify(newBeeMallShoppingCartItemMapper).selectCountByUserId(17L);
        }

        /* WB5 新商品成功添加到购物车 */
        @Test
        public void testSaveNewBeeMallCartItem_Success() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(1);

            NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
            newBeeMallGoods.setGoodsId(10147L);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(17L, 10147L)).thenReturn(null);
            when(newBeeMallGoodsMapper.selectByPrimaryKey(10147L)).thenReturn(newBeeMallGoods);
            when(newBeeMallShoppingCartItemMapper.selectCountByUserId(17L)).thenReturn(1);
            when(newBeeMallShoppingCartItemMapper.insertSelective(newCartItem)).thenReturn(1);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(17L,10147L);
            verify(newBeeMallGoodsMapper).selectByPrimaryKey(10147L);
            verify(newBeeMallShoppingCartItemMapper).selectCountByUserId(17L);
            verify(newBeeMallShoppingCartItemMapper).insertSelective(newCartItem);
        }

        /* WB6 数据库异常 */
        @Test
        public void testSaveNewBeeMallCartItem_Db_Error() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(1);

            NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
            newBeeMallGoods.setGoodsId(10147L);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(17L, 10147L)).thenReturn(null);
            when(newBeeMallGoodsMapper.selectByPrimaryKey(10147L)).thenReturn(newBeeMallGoods);
            when(newBeeMallShoppingCartItemMapper.selectCountByUserId(17L)).thenReturn(1);
            when(newBeeMallShoppingCartItemMapper.insertSelective(newCartItem)).thenReturn(0);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DB_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(17L,10147L);
            verify(newBeeMallGoodsMapper).selectByPrimaryKey(10147L);
            verify(newBeeMallShoppingCartItemMapper).selectCountByUserId(17L);
            verify(newBeeMallShoppingCartItemMapper).insertSelective(newCartItem);
        }


        /*
        updateNewBeeMallCartItem
         */

        /* WB1 购物车记录不存在 */
        @Test
        public void testUpdateNewBeeMallCartItem_DataNotExist(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(1L);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(1L)).thenReturn(null);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DATA_NOT_EXIST.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(1L);
        }

        /* WB2 商品数量超出限制 */
        @Test
        public void testUpdateNewBeeMallCartItem_ItemLimitError() {
            // 准备测试数据
            NewBeeMallShoppingCartItem existingCartItem = new NewBeeMallShoppingCartItem();
            existingCartItem.setCartItemId(1L);
            existingCartItem.setUserId(1L);
            existingCartItem.setGoodsCount(1);

            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(1L);
            newCartItem.setUserId(1L);
            newCartItem.setGoodsCount(Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER + 1);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(1L)).thenReturn(existingCartItem);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(1L);
        }

        /* WB3 数量相同不会进行修改 */
        @Test
        public void testUpdateNewBeeMallCartItem_Success_NoChange() {
            // 准备测试数据
            NewBeeMallShoppingCartItem existingCartItem = new NewBeeMallShoppingCartItem();
            existingCartItem.setCartItemId(1L);
            existingCartItem.setUserId(1L);
            existingCartItem.setGoodsCount(2);

            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(1L);
            newCartItem.setUserId(1L);
            newCartItem.setGoodsCount(2); // 数量相同

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(1L)).thenReturn(existingCartItem);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(1L);
        }

        /* WB4 userId不同不能修改 */
        @Test
        public void testUpdateNewBeeMallCartItem_NoPermissionError() {
            // 准备测试数据
            NewBeeMallShoppingCartItem existingCartItem = new NewBeeMallShoppingCartItem();
            existingCartItem.setCartItemId(1L);
            existingCartItem.setUserId(1L);
            existingCartItem.setGoodsCount(1);

            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(1L);
            newCartItem.setUserId(2L); // 不同的 userId
            newCartItem.setGoodsCount(2);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(1L)).thenReturn(existingCartItem);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.NO_PERMISSION_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(1L);
        }

        /* WB5 修改记录成功 */
        @Test
        public void testUpdateNewBeeMallCartItem_Success() {
            // 准备测试数据
            NewBeeMallShoppingCartItem existingCartItem = new NewBeeMallShoppingCartItem();
            existingCartItem.setCartItemId(1L);
            existingCartItem.setUserId(1L);
            existingCartItem.setGoodsCount(2);

            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(1L);
            newCartItem.setUserId(1L);
            newCartItem.setGoodsCount(3); // 不同的数量

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(1L)).thenReturn(existingCartItem);
            when(newBeeMallShoppingCartItemMapper.updateByPrimaryKeySelective(any(NewBeeMallShoppingCartItem.class))).thenReturn(1);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(1L);
            verify(newBeeMallShoppingCartItemMapper).updateByPrimaryKeySelective(any(NewBeeMallShoppingCartItem.class));
        }

        /* WB6 数据库异常 */
        @Test
        public void testUpdateNewBeeMallCartItem_Db_Error() {
            // 准备测试数据
            NewBeeMallShoppingCartItem existingCartItem = new NewBeeMallShoppingCartItem();
            existingCartItem.setCartItemId(1L);
            existingCartItem.setUserId(1L);
            existingCartItem.setGoodsCount(2);

            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(1L);
            newCartItem.setUserId(1L);
            newCartItem.setGoodsCount(3); // 不同的数量

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(1L)).thenReturn(existingCartItem);
            when(newBeeMallShoppingCartItemMapper.updateByPrimaryKeySelective(any(NewBeeMallShoppingCartItem.class))).thenReturn(0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DB_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(1L);
            verify(newBeeMallShoppingCartItemMapper).updateByPrimaryKeySelective(any(NewBeeMallShoppingCartItem.class));
        }

        /*
        getNewBeeMallCartItemById
         */
        /* WB1 存在的购物车条目 */
        @Test
        public void testGetNewBeeMallCartItemById_ItemExists(){
            // 准备测试数据
            NewBeeMallShoppingCartItem existingCartItem = new NewBeeMallShoppingCartItem();
            existingCartItem.setCartItemId(1L);
            existingCartItem.setUserId(1L);
            existingCartItem.setGoodsCount(2);

            long newBeeMallShoppingCartItemId = 158L;

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId)).thenReturn(existingCartItem);

            // 执行测试
            NewBeeMallShoppingCartItem result = newBeeMallShoppingCartService.getNewBeeMallCartItemById(newBeeMallShoppingCartItemId);

            // 验证结果
            assertEquals(existingCartItem, result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(158L);
        }

        @Test
        public void testGetNewBeeMallCartItemById_ItemDoesNotExist() {
            // 准备测试数据
            Long cartItemId = 2L;

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(cartItemId)).thenReturn(null);

            // 执行测试
            NewBeeMallShoppingCartItem result = newBeeMallShoppingCartService.getNewBeeMallCartItemById(cartItemId);

            // 验证结果
            assertNull(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(cartItemId);
        }

        @Test
        public void testGetNewBeeMallCartItemById_NullId() {
            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(null)).thenReturn(null);

            // 执行测试
            NewBeeMallShoppingCartItem result = newBeeMallShoppingCartService.getNewBeeMallCartItemById(null);

            // 验证结果
            assertNull(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(null);
        }

        /*
        deleteById
         */
        /* WB1 购物车条目不存在 */
        @Test
        public void testDeleteById_ItemDoesNotExist() {
            // 准备测试数据
            Long shoppingCartItemId = 1L;
            Long userId = 17L;

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId)).thenReturn(null);

            // 执行测试
            Boolean result = newBeeMallShoppingCartService.deleteById(shoppingCartItemId, userId);

            // 验证结果
            assertFalse(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(shoppingCartItemId);
        }

        /* WB2 userId不匹配 */
        @Test
        public void testDeleteById_UserIdMismatch() {
            // 准备测试数据
            Long shoppingCartItemId = 1L;
            Long userId = 17L;
            NewBeeMallShoppingCartItem cartItem = new NewBeeMallShoppingCartItem();
            cartItem.setCartItemId(shoppingCartItemId);
            cartItem.setUserId(18L); // 不同的userId

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId)).thenReturn(cartItem);

            // 执行测试
            Boolean result = newBeeMallShoppingCartService.deleteById(shoppingCartItemId, userId);

            // 验证结果
            assertFalse(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(shoppingCartItemId);
        }

        /* WB3 删除成功 */
        @Test
        public void testDeleteById_SuccessfulDeletion() {
            // 准备测试数据
            Long shoppingCartItemId = 1L;
            Long userId = 17L;
            NewBeeMallShoppingCartItem cartItem = new NewBeeMallShoppingCartItem();
            cartItem.setCartItemId(shoppingCartItemId);
            cartItem.setUserId(userId);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId)).thenReturn(cartItem);
            when(newBeeMallShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId)).thenReturn(1);

            // 执行测试
            Boolean result = newBeeMallShoppingCartService.deleteById(shoppingCartItemId, userId);

            // 验证结果
            assertTrue(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(shoppingCartItemId);
            verify(newBeeMallShoppingCartItemMapper).deleteByPrimaryKey(shoppingCartItemId);
        }

        /* WB4 删除失败 */
        @Test
        public void testDeleteById_DeletionFailed() {
            // 准备测试数据
            Long shoppingCartItemId = 1L;
            Long userId = 17L;
            NewBeeMallShoppingCartItem cartItem = new NewBeeMallShoppingCartItem();
            cartItem.setCartItemId(shoppingCartItemId);
            cartItem.setUserId(userId);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId)).thenReturn(cartItem);
            when(newBeeMallShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId)).thenReturn(0);

            // 执行测试
            Boolean result = newBeeMallShoppingCartService.deleteById(shoppingCartItemId, userId);

            // 验证结果
            assertFalse(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByPrimaryKey(shoppingCartItemId);
            verify(newBeeMallShoppingCartItemMapper).deleteByPrimaryKey(shoppingCartItemId);
        }

        /*
        getMyShoppingCartItems
         */

        /* WB1 购物车为空 */
        @Test
        public void testGetMyShoppingCartItems_EmptyCart() {
            // 准备测试数据
            Long userId = 1L;

            // 模拟行为：用户购物车为空
            when(newBeeMallShoppingCartItemMapper.selectByUserId(userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER))
                    .thenReturn(Collections.emptyList());

            List<NewBeeMallShoppingCartItemVO> result = newBeeMallShoppingCartService.getMyShoppingCartItems(userId);

            // 验证结果
            assertEquals(0, result.size());

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserId(userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        }

        /* WB2 购物车有商品但商品信息为空 */
        @Test
        public void testGetMyShoppingCartItems_NonEmptyCart_EmptyGoods() {
            // 准备测试数据
            Long userId = 1L;

            NewBeeMallShoppingCartItem cartItem = new NewBeeMallShoppingCartItem();
            cartItem.setCartItemId(1L);
            cartItem.setUserId(userId);
            cartItem.setGoodsId(101L);
            cartItem.setIsDeleted((byte)1);

            List<NewBeeMallShoppingCartItem> shoppingCartItems = Arrays.asList(cartItem);

            // 模拟行为：用户购物车有商品，但商品信息为空
            when(newBeeMallShoppingCartItemMapper.selectByUserId(userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER))
                    .thenReturn(shoppingCartItems);
            when(newBeeMallGoodsMapper.selectByPrimaryKeys(Collections.singletonList(101L)))
                    .thenReturn(Collections.emptyList());

            List<NewBeeMallShoppingCartItemVO> result = newBeeMallShoppingCartService.getMyShoppingCartItems(userId);

            // 验证结果
            assertEquals(0, result.size());

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserId(userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
            verify(newBeeMallGoodsMapper).selectByPrimaryKeys(Collections.singletonList(101L));
        }

        /* WB3 购物车和商品信息都不为空 */
        @Test
        public void testGetMyShoppingCartItems_NonEmptyCart_NonEmptyGoods() {
            // 准备测试数据
            Long userId = 1L;

            NewBeeMallShoppingCartItem cartItem = new NewBeeMallShoppingCartItem();
            cartItem.setCartItemId(1L);
            cartItem.setUserId(userId);
            cartItem.setGoodsId(101L);
            cartItem.setIsDeleted((byte)1);

            NewBeeMallGoods newBeeMallGoods=new NewBeeMallGoods();
            newBeeMallGoods.setGoodsId(101L);
            newBeeMallGoods.setGoodsName("Test Goods");
            newBeeMallGoods.setGoodsCoverImg("img.png");
            newBeeMallGoods.setSellingPrice(100);

            List<NewBeeMallShoppingCartItem> shoppingCartItems = Arrays.asList(cartItem);

            List<NewBeeMallGoods> goodsList = Arrays.asList(newBeeMallGoods);

            // 模拟行为：用户购物车和商品信息都不为空
            when(newBeeMallShoppingCartItemMapper.selectByUserId(userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER))
                    .thenReturn(shoppingCartItems);
            when(newBeeMallGoodsMapper.selectByPrimaryKeys(Collections.singletonList(101L)))
                    .thenReturn(goodsList);

            List<NewBeeMallShoppingCartItemVO> result = newBeeMallShoppingCartService.getMyShoppingCartItems(userId);

            // 验证结果
            assertEquals(1, result.size());
            NewBeeMallShoppingCartItemVO itemVO = result.get(0);
            assertEquals(101L, itemVO.getGoodsId());
            assertEquals("Test Goods", itemVO.getGoodsName());
            assertEquals("img.png", itemVO.getGoodsCoverImg());
            assertEquals(100, itemVO.getSellingPrice());

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserId(userId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
            verify(newBeeMallGoodsMapper).selectByPrimaryKeys(Collections.singletonList(101L));
        }

    }






    /* 黑盒测试 */
    @Nested
    class BlackBoxTests{
        @Autowired
        private NewBeeMallShoppingCartServiceImpl newBeeMallShoppingCartService;

        @Autowired
        private NewBeeMallShoppingCartItemMapper newBeeMallShoppingCartItemMapper;

        @Autowired
        private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

        private boolean compareNewBeeMallShoppingCartItem(NewBeeMallShoppingCartItem item1, NewBeeMallShoppingCartItem item2) {
            return item1.getCartItemId().equals(item2.getCartItemId()) &&
                    item1.getUserId().equals(item2.getUserId()) &&
                    item1.getGoodsId().equals(item2.getGoodsId()) &&
                    item1.getGoodsCount().equals(item2.getGoodsCount()) &&
                    item1.getIsDeleted().equals(item2.getIsDeleted());
        }

        /*
        saveNewBeeMallCartItem
         */

        /* BB1 覆盖等价类1，3，4，5 */
        @Test
        public void testSaveNewBeeMallCartItem_BB1() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(1);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);
        }

        /* BB2 覆盖等价类1，2，4，5 */
        @Test
        public void testSaveNewBeeMallCartItem_BB2() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(3);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);
        }

        /* BB3 覆盖等价类6 */
        @Test
        public void testSaveNewBeeMallCartItem_BB3() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(666L);//非法数据
            newCartItem.setGoodsId(10149L);
            newCartItem.setGoodsCount(3);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DB_ERROR.getResult(), result);
        }

        /* BB4 覆盖等价类7 */
        @Test
        public void testSaveNewBeeMallCartItem_BB4() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(1L);//非法数据
            newCartItem.setGoodsCount(1);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.GOODS_NOT_EXIST.getResult(), result);
        }

        /* BB5 覆盖等价类8 */
        @Test
        public void testSaveNewBeeMallCartItem_BB5() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER + 1);//非法数据

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult(), result);
        }
        /* BB6 覆盖等价类9 */
        @Test
        public void testSaveNewBeeMallCartItem_BB6() throws NoSuchFieldException, IllegalAccessException {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);

            // 通过反射设置非法数据
            Field field = NewBeeMallShoppingCartItem.class.getDeclaredField("goodsCount");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newCartItem, 3.5); // 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }
        /* BB7 覆盖等价类10 */
        @Test
        public void testSaveNewBeeMallCartItem_BB7() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(-2);//非法数据

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DB_ERROR.getResult(), result);
        }
        /* BB8 覆盖等价类11 */
        @Test
        public void testSaveNewBeeMallCartItem_BB8() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10283L);
            newCartItem.setGoodsCount(1);//非法数据

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult(), result);
        }
        /* BB9 覆盖等价类12 */
        //存疑，无法执行
        /*@Test
        public void testSaveNewBeeMallCartItem_BB9() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(1);//非法数据

            NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
            newBeeMallGoods.setGoodsId(10147L);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(17L, 10147L)).thenReturn(null);
            when(newBeeMallGoodsMapper.selectByPrimaryKey(10147L)).thenReturn(newBeeMallGoods);
            when(newBeeMallShoppingCartItemMapper.selectCountByUserId(17L)).thenReturn((int)8.5);
            when(newBeeMallShoppingCartItemMapper.insertSelective(newCartItem)).thenReturn(1);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DB_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(17L,10147L);
            verify(newBeeMallGoodsMapper).selectByPrimaryKey(10147L);
            verify(newBeeMallShoppingCartItemMapper).selectCountByUserId(17L);
            verify(newBeeMallShoppingCartItemMapper).insertSelective(newCartItem);
        }*/
        /* BB10 覆盖等价类13 */
        //存疑，无法执行
        /*@Test
        public void testSaveNewBeeMallCartItem_BB10() {
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setUserId(17L);
            newCartItem.setGoodsId(10147L);
            newCartItem.setGoodsCount(1);//非法数据

            NewBeeMallGoods newBeeMallGoods = new NewBeeMallGoods();
            newBeeMallGoods.setGoodsId(10147L);

            // 模拟行为
            when(newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(17L, 10147L)).thenReturn(null);
            when(newBeeMallGoodsMapper.selectByPrimaryKey(10147L)).thenReturn(newBeeMallGoods);
            when(newBeeMallShoppingCartItemMapper.selectCountByUserId(17L)).thenReturn(-3);
            when(newBeeMallShoppingCartItemMapper.insertSelective(newCartItem)).thenReturn(1);

            // 执行测试
            String result = newBeeMallShoppingCartService.saveNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DB_ERROR.getResult(), result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallShoppingCartItemMapper).selectByUserIdAndGoodsId(17L,10147L);
            verify(newBeeMallGoodsMapper).selectByPrimaryKey(10147L);
            verify(newBeeMallShoppingCartItemMapper).selectCountByUserId(17L);
            verify(newBeeMallShoppingCartItemMapper).insertSelective(newCartItem);
        }*/





        /*
        getNewBeeMallCartItemById
         */
        /* BB1 覆盖等价类1 */
        @Test
        public void testGetNewBeeMallCartItemById_BB1(){
            // 准备测试数据: 确保在测试数据库中存在此项
            Long cartItemId = 158L;

            // 执行测试
            NewBeeMallShoppingCartItem result = newBeeMallShoppingCartService.getNewBeeMallCartItemById(cartItemId);

            // 预期结果
            NewBeeMallShoppingCartItem expectedCartItem = new NewBeeMallShoppingCartItem();
            expectedCartItem.setCartItemId(cartItemId);
            expectedCartItem.setUserId(17L);
            expectedCartItem.setGoodsId(10147L);
            expectedCartItem.setGoodsCount(1);
            expectedCartItem.setIsDeleted((byte) 0);

            // 验证结果
            assertTrue(compareNewBeeMallShoppingCartItem(result,expectedCartItem));
        }

        /* BB2 覆盖等价类2 */
        @Test
        public void testGetNewBeeMallCartItemById_BB2(){
            // 准备测试数据
            Long cartItemId = 2000L;

            // 执行测试
            NewBeeMallShoppingCartItem result = newBeeMallShoppingCartService.getNewBeeMallCartItemById(cartItemId);

            // 验证结果
            assertNull(result);
        }



        /*
        updateNewBeeMallCartItem
         */

        /* BB1 覆盖等价类1，2，3 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB1(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsCount(2);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);
        }

        /* BB2 覆盖等价类1，2，4 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB2(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsCount(1);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SUCCESS.getResult(), result);
        }

        /* BB3 覆盖等价类5 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB3(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(666L);
            newCartItem.setGoodsCount(1);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertTrue(result.equals(ServiceResultEnum.NO_PERMISSION_ERROR.getResult())||result.equals(ServiceResultEnum.SUCCESS.getResult()));
        }

        /* BB4 覆盖等价类6 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB4(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(159L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsCount(1);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.NO_PERMISSION_ERROR.getResult(), result);
        }

        /* BB5 覆盖等价类7 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB5(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(2000L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsCount(1);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DATA_NOT_EXIST.getResult(), result);
        }

        /* BB6 覆盖等价类8 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB6(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsCount(7);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult(), result);
        }

        /* BB7 覆盖等价类9 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB7() throws NoSuchFieldException{
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setIsDeleted((byte)0);

            // 通过反射设置非法数据
            Field field = NewBeeMallShoppingCartItem.class.getDeclaredField("goodsCount");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newCartItem, 2.5); // 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB8 覆盖等价类10 */
        @Test
        public void testUpdateNewBeeMallCartItem_BB8(){
            // 准备测试数据
            NewBeeMallShoppingCartItem newCartItem = new NewBeeMallShoppingCartItem();
            newCartItem.setCartItemId(158L);
            newCartItem.setUserId(17L);
            newCartItem.setGoodsCount(-2);
            newCartItem.setIsDeleted((byte)0);

            // 执行测试
            String result = newBeeMallShoppingCartService.updateNewBeeMallCartItem(newCartItem);

            // 验证结果
            assertEquals(ServiceResultEnum.DB_ERROR.getResult(), result);
        }




        /*
        getMyShoppingCartItems
         */
        /* BB1 覆盖等价类1 */
        @Test
        public void testGetMyShoppingCartItems_BB1(){
            // 准备测试数据
            Long userId=17L;

            // 执行测试
            List<NewBeeMallShoppingCartItemVO> result=newBeeMallShoppingCartService.getMyShoppingCartItems(userId);

            // 验证结果
            assertEquals(13,result.size());
        }

        /* BB2 覆盖等价类2 */
        @Test
        public void testGetMyShoppingCartItems_BB2(){
            // 准备测试数据
            Long userId=16L;

            // 执行测试
            List<NewBeeMallShoppingCartItemVO> result=newBeeMallShoppingCartService.getMyShoppingCartItems(userId);

            // 验证结果
            assertEquals(0,result.size());
        }

        /* BB3 覆盖等价类3 */
        @Test
        public void testGetMyShoppingCartItems_BB3(){
            // 准备测试数据
            Long userId=200L;

            // 执行测试
            List<NewBeeMallShoppingCartItemVO> result=newBeeMallShoppingCartService.getMyShoppingCartItems(userId);

            // 验证结果
            assertEquals(0,result.size());
        }


        /*
        deleteById
         */

        /* BB1 覆盖等价类1,2 */
        @Test
        public void testDeleteById_BB1(){
            // 准备测试数据
            Long userId=666L;
            Long cartItemId=173L;

            // 执行测试
            Boolean result=newBeeMallShoppingCartService.deleteById(cartItemId,userId);

            // 验证结果
            assertTrue(result);
        }

        /* BB2 覆盖等价类3 */
        @Test
        public void testDeleteById_BB2(){
            // 准备测试数据
            Long userId=200L;
            Long cartItemId=160L;

            // 执行测试
            Boolean result=newBeeMallShoppingCartService.deleteById(cartItemId,userId);

            // 验证结果
            assertFalse(result);
        }

        /* BB3 覆盖等价类4 */
        @Test
        public void testDeleteById_BB3(){
            // 准备测试数据
            Long userId=17L;
            Long cartItemId=159L;

            // 执行测试
            Boolean result=newBeeMallShoppingCartService.deleteById(cartItemId,userId);

            // 验证结果
            assertFalse(result);
        }

        /* BB4 覆盖等价类5 */
        @Test
        public void testDeleteById_BB4(){
            // 准备测试数据
            Long userId=17L;
            Long cartItemId=2000L;

            // 执行测试
            Boolean result=newBeeMallShoppingCartService.deleteById(cartItemId,userId);

            // 验证结果
            assertFalse(result);
        }
    }
}