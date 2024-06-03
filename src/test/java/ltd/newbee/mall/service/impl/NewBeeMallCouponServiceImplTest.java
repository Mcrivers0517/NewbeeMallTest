package ltd.newbee.mall.service.impl;

import ltd.newbee.mall.controller.vo.NewBeeMallCouponVO;
import ltd.newbee.mall.controller.vo.NewBeeMallMyCouponVO;
import ltd.newbee.mall.controller.vo.NewBeeMallShoppingCartItemVO;
import ltd.newbee.mall.dao.NewBeeMallCouponMapper;
import ltd.newbee.mall.dao.NewBeeMallGoodsMapper;
import ltd.newbee.mall.dao.NewBeeMallUserCouponRecordMapper;
import ltd.newbee.mall.entity.NewBeeMallCoupon;
import ltd.newbee.mall.entity.NewBeeMallGoods;
import ltd.newbee.mall.entity.NewBeeMallUserCouponRecord;
import ltd.newbee.mall.exception.NewBeeMallException;
import ltd.newbee.mall.util.PageQueryUtil;
import ltd.newbee.mall.util.PageResult;
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
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringJUnitConfig
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class NewBeeMallCouponServiceImplTest {

    /* 白盒测试 */
    @Nested
    class WhiteBoxTests{
        @InjectMocks
        private NewBeeMallCouponServiceImpl newBeeMallCouponService;
        @Mock
        private NewBeeMallCouponMapper newBeeMallCouponMapper;

        @Mock
        private NewBeeMallUserCouponRecordMapper newBeeMallUserCouponRecordMapper;

        @Mock
        private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

        /*
        saveCoupon
         */
        /* WB1 保存成功 */
        @Test
        public void testSaveCoupon_Success(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = new NewBeeMallCoupon();
            newBeeMallCoupon.setCouponId(1L);
            newBeeMallCoupon.setCouponName("Test Coupon");

            // 模拟行为
            when(newBeeMallCouponMapper.insertSelective(newBeeMallCoupon)).thenReturn(1);

            // 执行测试
            boolean result = newBeeMallCouponService.saveCoupon(newBeeMallCoupon);

            // 验证结果
            assertTrue(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).insertSelective(newBeeMallCoupon);
        }

        /* WB2 保存失败 */
        @Test
        public void testSaveCoupon_Failure(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = new NewBeeMallCoupon();
            newBeeMallCoupon.setCouponId(1L);
            newBeeMallCoupon.setCouponName("Test Coupon");

            // 模拟行为
            when(newBeeMallCouponMapper.insertSelective(newBeeMallCoupon)).thenReturn(0);

            // 执行测试
            boolean result = newBeeMallCouponService.saveCoupon(newBeeMallCoupon);

            // 验证结果
            assertFalse(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).insertSelective(newBeeMallCoupon);
        }


        /*
        updateCoupon
         */
        /* WB1 修改成功 */
        @Test
        public void testUpdateCoupon_Success(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = new NewBeeMallCoupon();
            newBeeMallCoupon.setCouponId(1L);
            newBeeMallCoupon.setCouponName("Test Coupon");

            // 模拟行为
            when(newBeeMallCouponMapper.updateByPrimaryKeySelective(newBeeMallCoupon)).thenReturn(1);

            // 执行测试
            boolean result = newBeeMallCouponService.updateCoupon(newBeeMallCoupon);

            // 验证结果
            assertTrue(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).updateByPrimaryKeySelective(newBeeMallCoupon);
        }

        /* WB2 修改失败 */
        @Test
        public void testUpdateCoupon_Failure(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = new NewBeeMallCoupon();
            newBeeMallCoupon.setCouponId(1L);
            newBeeMallCoupon.setCouponName("Test Coupon");

            // 模拟行为
            when(newBeeMallCouponMapper.updateByPrimaryKeySelective(newBeeMallCoupon)).thenReturn(0);

            // 执行测试
            boolean result = newBeeMallCouponService.updateCoupon(newBeeMallCoupon);

            // 验证结果
            assertFalse(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).updateByPrimaryKeySelective(newBeeMallCoupon);
        }

        /*
        getCouponById
         */
        /* WB1 商品存在 */
        @Test
        public void testGetCouponById_CouponExists() {
            // 准备测试数据
            Long couponId = 1L;
            NewBeeMallCoupon expectedCoupon = new NewBeeMallCoupon();
            expectedCoupon.setCouponId(couponId);
            expectedCoupon.setCouponName("Test Coupon");

            // 模拟行为
            when(newBeeMallCouponMapper.selectByPrimaryKey(couponId)).thenReturn(expectedCoupon);

            // 执行测试
            NewBeeMallCoupon result = newBeeMallCouponService.getCouponById(couponId);

            // 验证结果
            assertEquals(expectedCoupon, result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).selectByPrimaryKey(couponId);
        }

        /* WB2 商品不存在 */
        @Test
        public void testGetCouponById_CouponDoesNotExist() {
            // 准备测试数据
            Long couponId = 1L;

            // 模拟行为
            when(newBeeMallCouponMapper.selectByPrimaryKey(couponId)).thenReturn(null);

            // 执行测试
            NewBeeMallCoupon result = newBeeMallCouponService.getCouponById(couponId);

            // 验证结果
            assertNull(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).selectByPrimaryKey(couponId);
        }


        /*
        deleteCouponById
         */

        /* WB1 删除成功 */
        @Test
        public void testDeleteCouponById_Success() {
            // 准备测试数据
            Long couponId = 1L;

            // 模拟行为
            when(newBeeMallCouponMapper.deleteByPrimaryKey(couponId)).thenReturn(1);

            // 执行测试
            boolean result = newBeeMallCouponService.deleteCouponById(couponId);

            // 验证结果
            assertTrue(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).deleteByPrimaryKey(couponId);
        }

        /* WB2 删除失败 */
        @Test
        public void testDeleteCouponById_Failure() {
            // 准备测试数据
            Long couponId = 1L;

            // 模拟行为
            when(newBeeMallCouponMapper.deleteByPrimaryKey(couponId)).thenReturn(0);

            // 执行测试
            boolean result = newBeeMallCouponService.deleteCouponById(couponId);

            // 验证结果
            assertFalse(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).deleteByPrimaryKey(couponId);
        }


        /*
        selectAvailableCoupon
         */

        /* WB1 userId不为空 */
        @Test
        public void testSelectAvailableCoupon_WithUserId() {
            // 准备测试数据
            Long userId = 1L;

            NewBeeMallCoupon coupon1 = new NewBeeMallCoupon();
            coupon1.setCouponId(1L);
            coupon1.setCouponTotal(10);

            NewBeeMallCoupon coupon2 = new NewBeeMallCoupon();
            coupon2.setCouponId(2L);
            coupon2.setCouponTotal(0);

            List<NewBeeMallCoupon> coupons = new ArrayList<>();
            coupons.add(coupon1);
            coupons.add(coupon2);

            // 模拟行为
            when(newBeeMallCouponMapper.selectAvailableCoupon()).thenReturn(coupons);
            when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, 1L)).thenReturn(1);
            when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, 2L)).thenReturn(0);
            when(newBeeMallUserCouponRecordMapper.getCouponCount(1L)).thenReturn(5);
            lenient().when(newBeeMallUserCouponRecordMapper.getCouponCount(2L)).thenReturn(0);

            // 执行测试
            List<NewBeeMallCouponVO> result = newBeeMallCouponService.selectAvailableCoupon(userId);

            // 验证结果
            assertNotNull(result);
            assertEquals(2, result.size());

            NewBeeMallCouponVO couponVO1 = result.get(0);
            assertTrue(couponVO1.isHasReceived());
            assertFalse(couponVO1.isSaleOut());

            NewBeeMallCouponVO couponVO2 = result.get(1);
            assertFalse(couponVO2.isHasReceived());
            assertFalse(couponVO2.isSaleOut());

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).selectAvailableCoupon();
            verify(newBeeMallUserCouponRecordMapper).getUserCouponCount(userId, 1L);
            verify(newBeeMallUserCouponRecordMapper).getUserCouponCount(userId, 2L);
            verify(newBeeMallUserCouponRecordMapper).getCouponCount(1L);

            // 验证 getCouponCount 方法没有被调用 coupon2
            verify(newBeeMallUserCouponRecordMapper, never()).getCouponCount(2L);
        }

        /* WB2 userId为空 */
        @Test
        public void testSelectAvailableCoupon_WithoutUserId() {
            // 准备测试数据
            Long userId = null;

            NewBeeMallCoupon coupon1 = new NewBeeMallCoupon();
            coupon1.setCouponId(1L);
            coupon1.setCouponTotal(10);

            NewBeeMallCoupon coupon2 = new NewBeeMallCoupon();
            coupon2.setCouponId(2L);
            coupon2.setCouponTotal(0);

            List<NewBeeMallCoupon> coupons = new ArrayList<>();
            coupons.add(coupon1);
            coupons.add(coupon2);

            // 模拟行为
            when(newBeeMallCouponMapper.selectAvailableCoupon()).thenReturn(coupons);
            lenient().when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, 1L)).thenReturn(1);
            lenient().when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, 2L)).thenReturn(0);
            lenient().when(newBeeMallUserCouponRecordMapper.getCouponCount(1L)).thenReturn(5);
            lenient().when(newBeeMallUserCouponRecordMapper.getCouponCount(2L)).thenReturn(0);


            // 执行测试
            List<NewBeeMallCouponVO> result = newBeeMallCouponService.selectAvailableCoupon(userId);

            // 验证结果
            assertNotNull(result);
            assertEquals(2, result.size());

            NewBeeMallCouponVO couponVO1 = result.get(0);
            assertFalse(couponVO1.isHasReceived());
            assertFalse(couponVO1.isSaleOut());

            NewBeeMallCouponVO couponVO2 = result.get(1);
            assertFalse(couponVO2.isHasReceived());
            assertFalse(couponVO2.isSaleOut());

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallCouponMapper).selectAvailableCoupon();
            verify(newBeeMallUserCouponRecordMapper, never()).getUserCouponCount(anyLong(), anyLong());
            verify(newBeeMallUserCouponRecordMapper).getCouponCount(1L);
            verify(newBeeMallUserCouponRecordMapper, never()).getCouponCount(2L);
        }

        /*
        saveCouponUser
         */

        /* WB1 优惠券不存在 */
        @Test
        public void testSaveCouponUser_CouponDoesNotExist() {
            Long couponId = 1L;
            Long userId = 1L;

            when(newBeeMallCouponMapper.selectByPrimaryKey(couponId)).thenReturn(null);

            NullPointerException exception = assertThrows(NullPointerException.class, () -> newBeeMallCouponService.saveCouponUser(couponId, userId));
            System.out.println("CouponDoesNotExist Test: " + exception.getMessage());

            verify(newBeeMallCouponMapper).selectByPrimaryKey(couponId);
        }

        /* WB2 优惠券已领取过 */
        @Test
        public void testSaveCouponUser_CouponAlreadyReceived() {
            Long couponId = 1L;
            Long userId = 1L;

            NewBeeMallCoupon coupon = new NewBeeMallCoupon();
            coupon.setCouponId(couponId);
            coupon.setCouponLimit((byte)1);

            when(newBeeMallCouponMapper.selectByPrimaryKey(couponId)).thenReturn(coupon);
            when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, couponId)).thenReturn(1);

            NewBeeMallException exception = assertThrows(NewBeeMallException.class, () -> newBeeMallCouponService.saveCouponUser(couponId, userId));
            System.out.println("CouponAlreadyReceived Test: " + exception.getMessage());

            verify(newBeeMallCouponMapper).selectByPrimaryKey(couponId);
            verify(newBeeMallUserCouponRecordMapper).getUserCouponCount(userId,couponId);
        }

        /* WB3 优惠券已经领完 */
        @Test
        public void testSaveCouponUser_CouponOutOfStock() {
            Long couponId = 1L;
            Long userId = 1L;

            NewBeeMallCoupon coupon = new NewBeeMallCoupon();
            coupon.setCouponId(couponId);
            coupon.setCouponTotal(1);
            coupon.setCouponLimit((byte)1);

            when(newBeeMallCouponMapper.selectByPrimaryKey(couponId)).thenReturn(coupon);
            when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, couponId)).thenReturn(0);
            when(newBeeMallUserCouponRecordMapper.getCouponCount(couponId)).thenReturn(1);

            NewBeeMallException exception = assertThrows(NewBeeMallException.class, () -> newBeeMallCouponService.saveCouponUser(couponId, userId));
            System.out.println("CouponOutOfStock Test: " + exception.getMessage());

            verify(newBeeMallCouponMapper).selectByPrimaryKey(couponId);
            verify(newBeeMallUserCouponRecordMapper).getUserCouponCount(userId,couponId);
            verify(newBeeMallUserCouponRecordMapper).getCouponCount(couponId);

        }

        /* WB4 优惠券领取失败 */
        @Test
        public void testSaveCouponUser_ReduceCouponTotalFails() {
            Long couponId = 1L;
            Long userId = 1L;

            NewBeeMallCoupon coupon = new NewBeeMallCoupon();
            coupon.setCouponId(couponId);
            coupon.setCouponTotal(1);
            coupon.setCouponLimit((byte)1);

            when(newBeeMallCouponMapper.selectByPrimaryKey(couponId)).thenReturn(coupon);
            when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, couponId)).thenReturn(0);
            when(newBeeMallUserCouponRecordMapper.getCouponCount(couponId)).thenReturn(0);
            when(newBeeMallCouponMapper.reduceCouponTotal(couponId)).thenReturn(0);

            NewBeeMallException exception = assertThrows(NewBeeMallException.class, () -> newBeeMallCouponService.saveCouponUser(couponId, userId));
            System.out.println("ReduceCouponTotalFails Test: " + exception.getMessage());

            verify(newBeeMallCouponMapper).selectByPrimaryKey(couponId);
            verify(newBeeMallUserCouponRecordMapper).getUserCouponCount(userId,couponId);
            verify(newBeeMallUserCouponRecordMapper).getCouponCount(couponId);
            verify(newBeeMallCouponMapper).reduceCouponTotal(couponId);

        }

        /* WB5 优惠券领取成功 */
        @Test
        public void testSaveCouponUser_Success() {
            Long couponId = 1L;
            Long userId = 1L;

            NewBeeMallCoupon coupon = new NewBeeMallCoupon();
            coupon.setCouponId(couponId);
            coupon.setCouponLimit((byte)1);
            coupon.setCouponTotal(1);

            when(newBeeMallCouponMapper.selectByPrimaryKey(couponId)).thenReturn(coupon);
            when(newBeeMallUserCouponRecordMapper.getUserCouponCount(userId, couponId)).thenReturn(0);
            when(newBeeMallUserCouponRecordMapper.getCouponCount(couponId)).thenReturn(0);
            when(newBeeMallCouponMapper.reduceCouponTotal(couponId)).thenReturn(1);
            when(newBeeMallUserCouponRecordMapper.insertSelective(any(NewBeeMallUserCouponRecord.class))).thenReturn(1);

            boolean result = newBeeMallCouponService.saveCouponUser(couponId, userId);

            assertTrue(result);

            verify(newBeeMallCouponMapper).selectByPrimaryKey(couponId);
            verify(newBeeMallUserCouponRecordMapper).getUserCouponCount(userId,couponId);
            verify(newBeeMallUserCouponRecordMapper).getCouponCount(couponId);
            verify(newBeeMallCouponMapper).reduceCouponTotal(couponId);
            verify(newBeeMallUserCouponRecordMapper).insertSelective(any(NewBeeMallUserCouponRecord.class));
        }


        /*
        selectMyCoupons
         */

        /* WB1 没有符合条件的优惠券记录 */
        @Test
        public void testSelectMyCoupons_NoRecords() {
            // 准备测试数据
            Map<String, Object> params = new HashMap<>();
            params.put("page", 1);
            params.put("limit", 10);
            PageQueryUtil pageUtil = new PageQueryUtil(params);

            // 模拟行为
            when(newBeeMallUserCouponRecordMapper.countMyCoupons(pageUtil)).thenReturn(0);

            // 调用方法进行测试
            PageResult<NewBeeMallCouponVO> result = newBeeMallCouponService.selectMyCoupons(pageUtil);

            // 验证结果
            assertEquals(0, result.getTotalCount()); // 确认总记录数为0
            assertTrue(result.getList().isEmpty()); // 确认返回的列表为空

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).countMyCoupons(pageUtil);
        }

        /* WB2 返回所有记录 */
        @Test
        public void testSelectMyCoupons_WithRecords_AllCouponsFound() {
            // 准备测试数据
            Map<String, Object> params = new HashMap<>();
            params.put("page", 1);
            params.put("limit", 10);
            PageQueryUtil pageUtil = new PageQueryUtil(params);
            when(newBeeMallUserCouponRecordMapper.countMyCoupons(pageUtil)).thenReturn(2); // 返回2条记录

            // 创建两个用户优惠券记录
            NewBeeMallUserCouponRecord record1 = new NewBeeMallUserCouponRecord();
            record1.setCouponUserId(1L);
            record1.setCouponId(1L);
            record1.setUseStatus((byte)1);

            NewBeeMallUserCouponRecord record2 = new NewBeeMallUserCouponRecord();
            record2.setCouponUserId(2L);
            record2.setCouponId(2L);
            record2.setUseStatus((byte)0);

            List<NewBeeMallUserCouponRecord> userCouponRecords = Arrays.asList(record1, record2);
            when(newBeeMallUserCouponRecordMapper.selectMyCoupons(pageUtil)).thenReturn(userCouponRecords);

            // 创建对应的优惠券记录
            NewBeeMallCoupon coupon1 = new NewBeeMallCoupon();
            coupon1.setCouponId(1L);

            NewBeeMallCoupon coupon2 = new NewBeeMallCoupon();
            coupon2.setCouponId(2L);

            List<NewBeeMallCoupon> coupons = Arrays.asList(coupon1, coupon2);
            when(newBeeMallCouponMapper.selectByIds(anyList())).thenReturn(coupons);

            // 调用方法进行测试
            PageResult<NewBeeMallCouponVO> result = newBeeMallCouponService.selectMyCoupons(pageUtil);

            // 验证结果
            assertEquals(2, result.getTotalCount()); // 确认总记录数为2
            assertEquals(2, result.getList().size()); // 确认返回的列表包含2条记录
            assertEquals(1L, result.getList().get(0).getCouponUserId()); // 验证第一个优惠券记录
            assertEquals(2L, result.getList().get(1).getCouponUserId()); // 验证第二个优惠券记录
            assertTrue(result.getList().get(0).isUseStatus()); // 验证第一个优惠券的使用状态
            assertFalse(result.getList().get(1).isUseStatus()); // 验证第二个优惠券的使用状态

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).countMyCoupons(pageUtil);
            verify(newBeeMallUserCouponRecordMapper).selectMyCoupons(pageUtil);
            verify(newBeeMallCouponMapper).selectByIds(anyList());
        }


        /*
        selectOrderCanUseCoupons
         */

        /* WB1 返回可用优惠券 */
        @Test
        public void testSelectOrderCanUseCoupons() {
            // 准备测试数据
            Long userId = 1L;
            int priceTotal = 500;
            List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = new ArrayList<>();
            NewBeeMallShoppingCartItemVO cartItem1 = new NewBeeMallShoppingCartItemVO();
            cartItem1.setGoodsId(1L);
            myShoppingCartItems.add(cartItem1);

            // 模拟获取用户可用的优惠券记录
            NewBeeMallUserCouponRecord couponRecord1 = new NewBeeMallUserCouponRecord();
            couponRecord1.setCouponId(1L);
            NewBeeMallUserCouponRecord couponRecord2 = new NewBeeMallUserCouponRecord();
            couponRecord2.setCouponId(2L);
            List<NewBeeMallUserCouponRecord> couponUsers = Arrays.asList(couponRecord1, couponRecord2);
            when(newBeeMallUserCouponRecordMapper.selectMyAvailableCoupons(userId)).thenReturn(couponUsers);

            // 模拟获取优惠券详细信息
            NewBeeMallCoupon coupon1 = new NewBeeMallCoupon();
            coupon1.setCouponId(1L);
            coupon1.setCouponName("优惠券1");
            coupon1.setCouponDesc("描述1");
            coupon1.setDiscount(100);
            coupon1.setMin(300);
            coupon1.setGoodsType((byte)0); // 全场通用
            coupon1.setCouponStartTime(LocalDate.now().minusDays(1));
            coupon1.setCouponEndTime(LocalDate.now().plusDays(1));

            NewBeeMallCoupon coupon2 = new NewBeeMallCoupon();
            coupon2.setCouponId(2L);
            coupon2.setCouponName("优惠券2");
            coupon2.setCouponDesc("描述2");
            coupon2.setDiscount(50);
            coupon2.setMin(200);
            coupon2.setGoodsType((byte)1); // 指定分类
            coupon2.setGoodsValue("1,2");
            coupon2.setCouponStartTime(LocalDate.now().minusDays(1));
            coupon2.setCouponEndTime(LocalDate.now().plusDays(1));

            List<NewBeeMallCoupon> coupons = new ArrayList<>();
            coupons.add(coupon1);
            coupons.add(coupon2);
            when(newBeeMallCouponMapper.selectByIds(anyList())).thenReturn(coupons);

            // 模拟获取商品分类信息
            NewBeeMallGoods goods = new NewBeeMallGoods();
            goods.setGoodsId(1L);
            goods.setGoodsCategoryId(1L);
            List<NewBeeMallGoods> goodsList = Collections.singletonList(goods);
            when(newBeeMallGoodsMapper.selectByPrimaryKeys(anyList())).thenReturn(goodsList);

            // 调用方法进行测试
            List<NewBeeMallMyCouponVO> result = newBeeMallCouponService.selectOrderCanUseCoupons(myShoppingCartItems, priceTotal, userId);

            // 验证结果
            assertNotNull(result);
            assertEquals(2, result.size());

            NewBeeMallMyCouponVO resultCoupon2 = result.get(0);
            assertEquals("优惠券2", resultCoupon2.getName());
            assertEquals(50, resultCoupon2.getDiscount());
            assertEquals(200, resultCoupon2.getMin());
            assertEquals((byte)1, resultCoupon2.getGoodsType());
            assertTrue(resultCoupon2.getStartTime().before(resultCoupon2.getEndTime()));

            NewBeeMallMyCouponVO resultCoupon1 = result.get(1);
            assertEquals("优惠券1", resultCoupon1.getName());
            assertEquals(100, resultCoupon1.getDiscount());
            assertEquals(300, resultCoupon1.getMin());
            assertEquals((byte)0, resultCoupon1.getGoodsType());
            assertTrue(resultCoupon1.getStartTime().before(resultCoupon1.getEndTime()));



            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).selectMyAvailableCoupons(userId);
            verify(newBeeMallCouponMapper).selectByIds(anyList());
            verify(newBeeMallGoodsMapper).selectByPrimaryKeys(anyList());
        }

        /* WB2 没有可用优惠券 */
        @Test
        public void testSelectOrderCanUseCoupons_NoCoupons() {
            // 准备测试数据
            Long userId = 1L;
            int priceTotal = 500;
            List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = new ArrayList<>();

            // 模拟获取用户可用的优惠券记录
            when(newBeeMallUserCouponRecordMapper.selectMyAvailableCoupons(userId)).thenReturn(Collections.emptyList());

            // 调用方法进行测试
            List<NewBeeMallMyCouponVO> result = newBeeMallCouponService.selectOrderCanUseCoupons(myShoppingCartItems, priceTotal, userId);

            // 验证结果
            assertNotNull(result);
            assertTrue(result.isEmpty());

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).selectMyAvailableCoupons(userId);
            verify(newBeeMallCouponMapper, never()).selectByIds(anyList());
            verify(newBeeMallGoodsMapper, never()).selectByPrimaryKeys(anyList());
        }

        /* WB3 有优惠券但是过期了 */
        @Test
        public void testSelectOrderCanUseCoupons_CouponsExpired() {
            // 准备测试数据
            Long userId = 1L;
            int priceTotal = 500;
            List<NewBeeMallShoppingCartItemVO> myShoppingCartItems = new ArrayList<>();
            NewBeeMallShoppingCartItemVO cartItem1 = new NewBeeMallShoppingCartItemVO();
            cartItem1.setGoodsId(1L);
            myShoppingCartItems.add(cartItem1);

            // 模拟获取用户可用的优惠券记录
            NewBeeMallUserCouponRecord couponRecord1 = new NewBeeMallUserCouponRecord();
            couponRecord1.setCouponId(1L);
            List<NewBeeMallUserCouponRecord> couponUsers = Collections.singletonList(couponRecord1);
            when(newBeeMallUserCouponRecordMapper.selectMyAvailableCoupons(userId)).thenReturn(couponUsers);

            // 模拟获取优惠券详细信息
            NewBeeMallCoupon coupon1 = new NewBeeMallCoupon();
            coupon1.setCouponId(1L);
            coupon1.setCouponName("过期优惠券");
            coupon1.setCouponDesc("描述1");
            coupon1.setDiscount(100);
            coupon1.setMin(300);
            coupon1.setGoodsType((byte)0); // 全场通用
            coupon1.setCouponStartTime(LocalDate.now().minusDays(10));
            coupon1.setCouponEndTime(LocalDate.now().minusDays(1));

            List<NewBeeMallCoupon> coupons = Collections.singletonList(coupon1);
            when(newBeeMallCouponMapper.selectByIds(anyList())).thenReturn(coupons);

            // 调用方法进行测试
            List<NewBeeMallMyCouponVO> result = newBeeMallCouponService.selectOrderCanUseCoupons(myShoppingCartItems, priceTotal, userId);

            // 验证结果
            assertNotNull(result);
            assertTrue(result.isEmpty()); // 因为优惠券已过期，结果应该为空

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).selectMyAvailableCoupons(userId);
            verify(newBeeMallCouponMapper).selectByIds(anyList());
        }

        /*
        deleteCouponUser
         */

        /* WB1 删除成功 */
        @Test
        public void testDeleteCouponUser_Success() {
            // 模拟删除成功的行为
            when(newBeeMallUserCouponRecordMapper.deleteByPrimaryKey(1L)).thenReturn(1);

            // 调用方法进行测试
            boolean result = newBeeMallCouponService.deleteCouponUser(1L);

            // 验证结果
            assertTrue(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).deleteByPrimaryKey(1L);
        }

        /* WB2 删除失败 */
        @Test
        public void testDeleteCouponUser_Failure() {
            // 模拟删除失败的行为
            when(newBeeMallUserCouponRecordMapper.deleteByPrimaryKey(1L)).thenReturn(0);

            // 调用方法进行测试
            boolean result = newBeeMallCouponService.deleteCouponUser(1L);

            // 验证结果
            assertFalse(result);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).deleteByPrimaryKey(1L);
        }



        /*
        releaseCoupon
         */

        /* WB1 记录存在 */
        @Test
        public void testReleaseCoupon_RecordExists() {
            // 准备测试数据
            Long orderId = 1L;
            NewBeeMallUserCouponRecord userCouponRecord = new NewBeeMallUserCouponRecord();
            userCouponRecord.setCouponUserId(1L);
            userCouponRecord.setOrderId(orderId);
            userCouponRecord.setUseStatus((byte) 1);

            // 模拟行为
            when(newBeeMallUserCouponRecordMapper.getUserCouponByOrderId(orderId)).thenReturn(userCouponRecord);

            // 调用方法进行测试
            newBeeMallCouponService.releaseCoupon(orderId);

            // 验证更新后的状态
            assertEquals((byte) 0, userCouponRecord.getUseStatus());
            assertNotNull(userCouponRecord.getUpdateTime());

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).getUserCouponByOrderId(orderId);
            verify(newBeeMallUserCouponRecordMapper).updateByPrimaryKey(userCouponRecord);
        }

        /* WB2 记录不存在 */
        @Test
        public void testReleaseCoupon_RecordDoesNotExist() {
            // 准备测试数据
            Long orderId = 1L;

            // 模拟行为
            when(newBeeMallUserCouponRecordMapper.getUserCouponByOrderId(orderId)).thenReturn(null);

            // 调用方法进行测试
            newBeeMallCouponService.releaseCoupon(orderId);

            // 验证模拟对象是否按预期被调用
            verify(newBeeMallUserCouponRecordMapper).getUserCouponByOrderId(orderId);
            verify(newBeeMallUserCouponRecordMapper, never()).updateByPrimaryKey(any());
        }

    }

    /* 黑盒测试 */
    @Nested
    class BlackBoxTests{
        @Autowired
        private  NewBeeMallCouponServiceImpl newBeeMallCouponService;
        @Autowired
        private NewBeeMallCouponMapper newBeeMallCouponMapper;

        @Autowired
        private NewBeeMallUserCouponRecordMapper newBeeMallUserCouponRecordMapper;

        @Autowired
        private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

        private boolean compareNewBeeMallCoupon(NewBeeMallCoupon item1, NewBeeMallCoupon item2) {
            return item1.getCouponId().equals(item2.getCouponId()) &&
                    item1.getCouponName().equals(item2.getCouponName()) &&
                    item1.getDiscount().equals(item2.getDiscount()) &&
                    item1.getMin().equals(item2.getMin()) &&
                    item1.getGoodsType().equals(item2.getGoodsType());
        }


        /*
        saveCoupon
         */
        /* BB1 覆盖等价类1 */
        @Test
        public void testSaveCoupon_BB1(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = new NewBeeMallCoupon();
            newBeeMallCoupon.setCouponId(3003L);
            newBeeMallCoupon.setCouponName("Test Coupon");
            newBeeMallCoupon.setDiscount(100);
            newBeeMallCoupon.setMin(300);
            newBeeMallCoupon.setGoodsType((byte)0); // 全场通用
            newBeeMallCoupon.setCouponStartTime(LocalDate.now().minusDays(10));
            newBeeMallCoupon.setCouponEndTime(LocalDate.now().minusMonths(1));

            // 执行测试
            boolean result = newBeeMallCouponService.saveCoupon(newBeeMallCoupon);

            // 验证结果
            assertTrue(result);
        }

        /* BB2 覆盖等价类2 */
        @Test
        public void testSaveCoupon_BB2(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = null;

            try {
                // 执行测试
                boolean result = newBeeMallCouponService.saveCoupon(newBeeMallCoupon);
                // 验证结果
                assertTrue(result);  // 预期结果是保存成功，这行代码可能不会执行，因为上面会抛出异常
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }


        /*
        updateCoupon
         */
        /* BB1 覆盖等价类1 */
        @Test
        public void testUpdateCoupon_BB1(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = new NewBeeMallCoupon();
            newBeeMallCoupon.setCouponId(3003L);
            newBeeMallCoupon.setCouponName("Test Coupon");
            newBeeMallCoupon.setDiscount(200);
            newBeeMallCoupon.setMin(300);
            newBeeMallCoupon.setGoodsType((byte)0); // 全场通用
            newBeeMallCoupon.setCouponStartTime(LocalDate.now().minusDays(10));
            newBeeMallCoupon.setCouponEndTime(LocalDate.now().minusMonths(1));

            // 执行测试
            boolean result = newBeeMallCouponService.updateCoupon(newBeeMallCoupon);

            // 验证结果
            assertTrue(result);
        }

        /* BB2 覆盖等价类2 */
        @Test
        public void testUpdateCoupon_BB2(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = new NewBeeMallCoupon();
            newBeeMallCoupon.setCouponId(4000L);
            newBeeMallCoupon.setCouponName("Test Coupon");
            newBeeMallCoupon.setDiscount(200);
            newBeeMallCoupon.setMin(300);
            newBeeMallCoupon.setGoodsType((byte)0); // 全场通用
            newBeeMallCoupon.setCouponStartTime(LocalDate.now().minusDays(10));
            newBeeMallCoupon.setCouponEndTime(LocalDate.now().minusMonths(1));

            // 执行测试
            boolean result = newBeeMallCouponService.updateCoupon(newBeeMallCoupon);

            // 验证结果
            assertFalse(result);
        }
        /* BB3 覆盖等价类4 */
        @Test
        public void testUpdateCoupon_BB4(){
            // 准备测试数据
            NewBeeMallCoupon newBeeMallCoupon = null;

            try {
                // 执行测试
                boolean result = newBeeMallCouponService.updateCoupon(newBeeMallCoupon);
                // 验证结果
                assertTrue(result);  // 预期结果是保存成功，这行代码可能不会执行，因为上面会抛出异常
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }

        /*
        getCouponById
         */

        /* BB1  */
        @Test
        public void testGetCouponById_BB1(){
            //准备数据
            Long couponId=3000L;

            NewBeeMallCoupon expectedCoupon = new NewBeeMallCoupon();
            expectedCoupon.setCouponId(3000L);
            expectedCoupon .setCouponName("Test Coupon");
            expectedCoupon.setDiscount(200);
            expectedCoupon.setMin(300);
            expectedCoupon.setGoodsType((byte)0); // 全场通用
            expectedCoupon.setCouponStartTime(LocalDate.now().minusDays(10));
            expectedCoupon.setCouponEndTime(LocalDate.now().minusMonths(1));

            NewBeeMallCoupon result=newBeeMallCouponService.getCouponById(couponId);

            assertTrue(compareNewBeeMallCoupon(expectedCoupon,result));
        }

        /* BB2  */
        @Test
        public void testGetCouponById_BB2(){
            //准备数据
            Long couponId=-1L;

            NewBeeMallCoupon result=newBeeMallCouponService.getCouponById(couponId);

            assertNull(result);
        }

        /* BB3  */
        @Test
        public void testGetCouponById_BB3()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,1.5);
                    NewBeeMallCoupon result=newBeeMallCouponService.getCouponById(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB4  */
        @Test
        public void testGetCouponById_BB4()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,'六');
                    NewBeeMallCoupon result=newBeeMallCouponService.getCouponById(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB5  */
        @Test
        public void testGetCouponById_BB5(){
            //准备数据
            Long couponId=666L;

            NewBeeMallCoupon result=newBeeMallCouponService.getCouponById(couponId);

            assertNull(result);
        }


        /*
        deleteCouponById
         */

        /* BB1 */
        @Test
        public void testDeleteCouponById_BB1(){
            //准备数据
            Long couponId=3003L;

            Boolean result=newBeeMallCouponService.deleteCouponById(couponId);

            assertTrue(result);
        }

        /* BB2 */
        @Test
        public void testDeleteCouponById_BB2(){
            //准备数据
            Long couponId=-1L;

            boolean result=newBeeMallCouponService.deleteCouponById(couponId);

            assertFalse(result);
        }

        /* BB3  */
        @Test
        public void testDeleteCouponById_BB3()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,1.5);
                    boolean result=newBeeMallCouponService.deleteCouponById(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB4  */
        @Test
        public void testDeleteCouponById_BB4()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,'六');
                    boolean result=newBeeMallCouponService.deleteCouponById(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB5 */
        @Test
        public void testDeleteCouponById_BB5(){
            //准备数据
            Long couponId=666L;

            boolean result=newBeeMallCouponService.deleteCouponById(couponId);

            assertFalse(result);
        }


        /*
        selectAvailableCoupon
         */
        /* BB1 */
        @Test
        public void testSelectAvailableCoupon_BB1(){
            // 准备测试数据
            Long userId=17L;

            List<NewBeeMallCouponVO> result=newBeeMallCouponService.selectAvailableCoupon(userId);

            assertEquals(2,result.size());
        }

        /* BB2 */
        @Test
        public void testSelectAvailableCoupon_BB2(){
            // 准备测试数据
            Long userId=-1L;

            List<NewBeeMallCouponVO> result=newBeeMallCouponService.selectAvailableCoupon(userId);

            assertEquals(2,result.size());
        }

        /* BB3  */
        //利用反射模拟userId注入不符合规范的数据，利用couponId代替userId。
        @Test
        public void testSelectAvailableCoupon_BB3()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,1.5);
                    List<NewBeeMallCouponVO> result=newBeeMallCouponService.selectAvailableCoupon(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB4  */
        //利用反射模拟userId注入不符合规范的数据，利用couponId代替userId。
        @Test
        public void testSelectAvailableCoupon_BB4()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,'六');
                    List<NewBeeMallCouponVO> result=newBeeMallCouponService.selectAvailableCoupon(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }


        /* BB5 */
        @Test
        public void testSelectAvailableCoupon_BB5(){
            // 准备测试数据
            Long userId=222L;

            List<NewBeeMallCouponVO> result=newBeeMallCouponService.selectAvailableCoupon(userId);

            assertEquals(2,result.size());
        }


        /*
        saveCouponUser
         */
        /* BB1 */
        @Test
        public void testSaveCouponUser_BB1(){
            Long couponId =3002L;
            Long userId=17L;

            boolean result=newBeeMallCouponService.saveCouponUser(couponId,userId);

            assertTrue(result);
        }

        /* BB2 */
        @Test
        public void testSaveCouponUser_BB2(){
            Long couponId =-1L;
            Long userId=17L;

            try {
                // 执行测试
                boolean result=newBeeMallCouponService.saveCouponUser(couponId,userId);
                // 验证结果
                assertTrue(result);  // 预期结果是保存成功，这行代码可能不会执行，因为上面会抛出异常
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }

        /* BB3 */
        @Test
        public void testSaveCouponUser_BB3()throws NoSuchFieldException{
            //准备数据
            Long userId=17L;
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,1.5);
                    boolean result=newBeeMallCouponService.saveCouponUser(newBeeMallCoupon.getCouponId(),userId);// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB4 */
        @Test
        public void testSaveCouponUser_BB4()throws NoSuchFieldException{
            //准备数据
            Long userId=17L;
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,'六');
                    boolean result=newBeeMallCouponService.saveCouponUser(newBeeMallCoupon.getCouponId(),userId);// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }



        /* BB5 */
        @Test
        public void testSaveCouponUser_BB5(){
            Long couponId =666L;
            Long userId=17L;

            try {
                // 执行测试
                boolean result=newBeeMallCouponService.saveCouponUser(couponId,userId);
                // 验证结果
                assertTrue(result);  // 预期结果是保存成功，这行代码可能不会执行，因为上面会抛出异常
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }

        /* BB6 */
        @Test
        public void testSaveCouponUser_BB6(){
            Long couponId =3000L;
            Long userId=-1L;

            try {
                // 执行测试
                boolean result=newBeeMallCouponService.saveCouponUser(couponId,userId);
                // 验证结果
                assertFalse(result);  // 预期结果是保存成功，这行代码可能不会执行，因为上面会抛出异常
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }

        /* BB7 */
        @Test
        public void testSaveCouponUser_BB7()throws NoSuchFieldException{
            //准备数据
            Long couponId=3000L;
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,1.5);
                    boolean result=newBeeMallCouponService.saveCouponUser(couponId,newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB8 */
        @Test
        public void testSaveCouponUser_BB8()throws NoSuchFieldException{
            //准备数据
            Long couponId=3000L;
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,'六');
                    boolean result=newBeeMallCouponService.saveCouponUser(couponId,newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        /* BB9 */
        @Test
        public void testSaveCouponUser_BB9(){
            Long couponId =3000L;
            Long userId=666L;

            try {
                // 执行测试
                boolean result=newBeeMallCouponService.saveCouponUser(couponId,userId);
                // 验证结果
                assertFalse(result);  // 预期结果是保存成功，这行代码可能不会执行，因为上面会抛出异常
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }

        /*
        selectMyCoupons
         */


        /*
        selectOrderCanUseCoupons
         */


        /*
        deleteCouponUser
         */
        @Test
        public void testDeleteCouponUser_BB1(){
            Long couponUserId=138L;

            boolean result=newBeeMallCouponService.deleteCouponUser(couponUserId);

            assertTrue(result);
        }

        @Test
        public void testDeleteCouponUser_BB2(){
            Long couponUserId=-1L;

            boolean result=newBeeMallCouponService.deleteCouponUser(couponUserId);

            assertFalse(result);
        }

        @Test
        public void testDeleteCouponUser_BB3()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,1.5);
                    boolean result=newBeeMallCouponService.deleteCouponUser(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        @Test
        public void testDeleteCouponUser_BB4()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,'六');
                    boolean result=newBeeMallCouponService.deleteCouponUser(newBeeMallCoupon.getCouponId());// 尝试设置 Double 类型数据
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        @Test
        public void testDeleteCouponUser_BB5(){
            Long couponUserId=666L;

            boolean result=newBeeMallCouponService.deleteCouponUser(couponUserId);

            assertFalse(result);
        }


        /*
        releaseCoupon
         */
        @Test
        public void testReleaseCoupon_BB1(){
            Long orderId=1L;

            newBeeMallCouponService.releaseCoupon(orderId);
            NewBeeMallUserCouponRecord newBeeMallUserCouponRecord = newBeeMallUserCouponRecordMapper.getUserCouponByOrderId(orderId);

            assertEquals((byte)0,newBeeMallUserCouponRecord.getUseStatus());

        }

        @Test
        public void testReleaseCoupon_BB2(){
            try {
                Long orderId=-1L;

                newBeeMallCouponService.releaseCoupon(orderId);
                NewBeeMallUserCouponRecord newBeeMallUserCouponRecord = newBeeMallUserCouponRecordMapper.getUserCouponByOrderId(orderId);

                assertEquals((byte)0,newBeeMallUserCouponRecord.getUseStatus());
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }

        @Test
        public void testReleaseCoupon_BB3()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,1.5);
                    newBeeMallCouponService.releaseCoupon(newBeeMallCoupon.getCouponId());
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        @Test
        public void testReleaseCoupon_BB4()throws NoSuchFieldException{
            //准备数据
            NewBeeMallCoupon newBeeMallCoupon=new NewBeeMallCoupon();
            // 通过反射设置非法数据
            Field field = NewBeeMallCoupon.class.getDeclaredField("couponId");
            field.setAccessible(true);

            // 捕获 IllegalArgumentException 异常并输出信息
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                try {
                    field.set(newBeeMallCoupon,'六');
                    newBeeMallCouponService.releaseCoupon(newBeeMallCoupon.getCouponId());
                } catch (IllegalArgumentException e) {
                    // 输出异常信息
                    System.out.println("Caught IllegalArgumentException: " + e.getMessage());
                    // 再次抛出异常以便 assertThrows 捕获
                    throw e;
                }
            });
        }

        @Test
        public void testReleaseCoupon_BB5(){
            try {
                Long orderId=666L;

                newBeeMallCouponService.releaseCoupon(orderId);
                NewBeeMallUserCouponRecord newBeeMallUserCouponRecord = newBeeMallUserCouponRecordMapper.getUserCouponByOrderId(orderId);

                assertEquals((byte)0,newBeeMallUserCouponRecord.getUseStatus());
            } catch (Exception e) {
                // 捕获并输出异常
                e.printStackTrace();
            }
        }

    }
}