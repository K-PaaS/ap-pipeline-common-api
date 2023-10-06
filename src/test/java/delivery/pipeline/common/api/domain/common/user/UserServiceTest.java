package delivery.pipeline.common.api.domain.common.user;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import delivery.pipeline.common.api.common.CommonService;
import delivery.pipeline.common.api.common.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created by hrjin on 2017-06-09.
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {
    private final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    private static final String userTid = "test-user-" + UUID.randomUUID().toString();
    private static final String name = "test";
    private static final String tellPhone = "010-2234-4521";
    private static final String cellPhone = "010-2233-4091";
    private static final String email = "hyunlee@bluedigm.com";
    private static final String company = "BD";
    private static final String description = "aaa";
    private static final Date created = new Date();
    private static final Date lastModified = new Date();
    private static final String createdString = "bbb";
    private static final String lastModifiedString = "ccc";

    private static final int PAGE_COUNT = 9999;
    private static final int PAGE_SIZE = 1;
    private static final int TOTAL_PAGES = 1;
    private static final long TOTAL_ELEMENTS = 1;

    private static User gTestUserModel = null;
    private static User gTestResultUserModel = null;
    private static Pageable gTestPageable = null;
    private static List<User> gTestUserList = null;
    private static UserList gTestResultUserList = null;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommonService commonService;

    @Mock
    private Page<User> userPage;

    @InjectMocks
    private UserService userService;



    @Before
    public void setUp() throws Exception{
        gTestPageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return 1;
            }

            @Override
            public int getPageSize() {
                return 1;
            }

            @Override
            public int getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        };


        gTestUserModel = new User();
        gTestResultUserModel = new User();
        gTestResultUserList = new UserList();
        gTestUserList = new ArrayList<>();

        gTestUserModel.setId(userTid);
        gTestUserModel.setName(name);
        gTestUserModel.setTellPhone(tellPhone);
        gTestUserModel.setCellPhone(cellPhone);
        gTestUserModel.setEmail(email);
        gTestUserModel.setCompany(company);
        gTestUserModel.setDescription(description);
        gTestUserModel.setCreated(created);
        gTestUserModel.setLastModified(lastModified);
        gTestUserModel.setCreatedString(createdString);
        gTestUserModel.setLastModifiedString(lastModifiedString);

        gTestResultUserModel.setId(userTid);
        gTestResultUserModel.setName(name);
        gTestResultUserModel.setTellPhone(tellPhone);
        gTestResultUserModel.setCellPhone(cellPhone);
        gTestResultUserModel.setEmail(email);
        gTestResultUserModel.setCompany(company);
        gTestResultUserModel.setDescription(description);
        gTestResultUserModel.setCreated(created);
        gTestResultUserModel.setLastModified(lastModified);
        gTestResultUserModel.setCreatedString(createdString);
        gTestResultUserModel.setLastModifiedString(lastModifiedString);

        gTestUserList.add(gTestUserModel);

        userPage = new PageImpl<>(gTestUserList);

        gTestResultUserList.setUsers(gTestUserList);
        gTestResultUserList.setPage(PAGE_COUNT);
        gTestResultUserList.setSize(PAGE_SIZE);
        gTestResultUserList.setTotalElements(TOTAL_ELEMENTS);
        gTestResultUserList.setTotalPages(TOTAL_PAGES);
        gTestResultUserList.setLast(true);

    }

    @After
    public void tearDown() throws Exception{
    }


    /**
     * Gets User list valid return list.
     *
     * @throws Exception
     */
    @Test
    public void getUserListPageable_Valid_ReturnList() throws Exception{

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(userRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(userPage);
        when(commonService.setPageInfo(any(Page.class), anyObject())).thenReturn(gTestResultUserList);

        UserList resultList = userService.getUserList(null, gTestPageable);

        assertThat(resultList).isNotNull();
        assertEquals(gTestResultUserList, resultList);
        assertEquals(PAGE_COUNT, resultList.getPage());
        assertEquals(PAGE_SIZE, resultList.getSize());
        assertEquals(TOTAL_PAGES, resultList.getTotalPages());
        assertEquals(TOTAL_ELEMENTS, resultList.getTotalElements());
        assertEquals(true, resultList.isLast());
        assertEquals(userTid, resultList.getUsers().get(0).getId());
        assertEquals(name, resultList.getUsers().get(0).getName());
    }

    @Test
    public void getUser_Valid_ReturnModel() throws Exception{
        when(userRepository.findOne(userTid)).thenReturn(gTestResultUserModel);

        User resultModel = userService.getUser(userTid);

        assertThat(resultModel).isNotNull();
        assertEquals(userTid, resultModel.getId());
        assertEquals(tellPhone, resultModel.getTellPhone());
        assertEquals(cellPhone, resultModel.getCellPhone());
        assertEquals(email, resultModel.getEmail());
        assertEquals(company, resultModel.getCompany());
        assertEquals(description, resultModel.getDescription());
        assertEquals(created, gTestResultUserModel.getCreated());
        assertEquals(lastModified, gTestResultUserModel.getLastModified());
        assertNotEquals(createdString, gTestResultUserModel.getCreatedString());
        assertNotEquals(lastModifiedString, gTestResultUserModel.getLastModifiedString());
    }


    @Test
    public void createUser_Valid_ReturnModel() throws Exception{
        when(userRepository.save(gTestUserModel)).thenReturn(gTestResultUserModel);

        User resultModel = userService.createUser(gTestUserModel);

        assertThat(resultModel).isNotNull();
        assertEquals(userTid, resultModel.getId());
        assertEquals(name, resultModel.getName());
    }

    @Test
    public void updateUser_Valid_ReturnModel() throws Exception{
        gTestUserModel.setId(userTid);

        when(userRepository.save(gTestUserModel)).thenReturn(gTestResultUserModel);


        User resultModel = userService.updateUser(gTestUserModel);

        assertThat(resultModel).isNotNull();
        assertEquals(userTid, resultModel.getId());

    }

    @Test
    public void deleteUser_Valid_ReturnString() throws Exception{
        doNothing().when(userRepository).delete(userTid);

        String resultString = userService.deleteUser(userTid);

        assertThat(resultString).isNotNull();
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultString);
    }
}
