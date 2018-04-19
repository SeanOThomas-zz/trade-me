package com.sean.thomas.trademe

import com.sean.thomas.trademe.categories.CategoriesContract
import com.sean.thomas.trademe.categories.CategoriesPresenter
import com.sean.thomas.trademe.network.Repository
import com.sean.thomas.trademe.network.models.Category
import com.sean.thomas.trademe.util.TestSchedulersProvider
import io.reactivex.Flowable
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class CategoriesPresenterUnitTest {

    companion object {
        const val ROOT_CAT_ID = ""
        const val FIRST_LEVEL_CAT_ID = "1234-"
        const val SECOND_LEVEL_CAT_ID = "5678-"
    }

    @Mock
    private lateinit var view: CategoriesContract.View

    @Mock
    private lateinit var repository: Repository

    private lateinit var presenter: CategoriesContract.Presenter

    private lateinit var categoryTree: Category

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        setUpCategoryTree();

        // set up presenter
        presenter = CategoriesPresenter(view, repository,  TestSchedulersProvider)
    }

    @After
    fun tearDown() {}

    @Test
    fun test_setUp_setsCategoriesAfterSuccess() {
        // arrange
        Mockito.`when`(repository.getCategoryTree()).thenReturn(Flowable.just(categoryTree))

        // act
        presenter.setUp()

        // assert
        verify(view).showProgress()
        verify(view).hideProgress()
        verify(view).setCategories(categoryTree.subCategories?: ArrayList())
    }

    @Test
    fun test_setUp_showsNetworkErrorMessageAfterError() {
        // arrange
        Mockito.`when`(repository.getCategoryTree()).thenReturn(Flowable.error(Throwable()))

        // act
        presenter.setUp()

        // assert
        verify(view).showProgress()
        verify(view).hideProgress()
        verify(view, never()).setCategories(categoryTree.subCategories?: ArrayList())
        verify(view).showNetworkErrorMessage()
    }

    @Test
    fun test_onCategoryClicked_setsCategoriesIfSubcategories() {
        // act
        presenter.onCategoryClicked(categoryTree)

        // assert
        verify(view).setCategories(categoryTree.subCategories?: ArrayList())
    }

    @Test
    fun test_onCategoryClicked_shallNotSetCategoriesIfNullSubCategories() {
        // arrange
        val category = Category("NA", "123", null)

        // act
        presenter.onCategoryClicked(category)

        // assert
        verify(view, never()).setCategories(category.subCategories?: ArrayList())
    }

    @Test
    fun test_onBackPressed_finishesIfEmptyRoot() {
        // act
        presenter.onBackPressed()

        // assert
        verify(view).finish()
    }

    @Test
    fun test_getRootCategory() {
        // act
        val result = (presenter as CategoriesPresenter).getRootCategory(categoryTree, SECOND_LEVEL_CAT_ID)

        // assert
        assertEquals(ROOT_CAT_ID, result.categoryId)
    }

    private fun setUpCategoryTree() {
        val cat2 = Category("NA", SECOND_LEVEL_CAT_ID, listOf())
        val cat1 = Category("NA", FIRST_LEVEL_CAT_ID, listOf(cat2))

        categoryTree = Category("Root", "", listOf(cat1))
    }
}