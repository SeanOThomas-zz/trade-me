package com.sean.thomas.trademe

import com.sean.thomas.trademe.listings.ListingsContract
import com.sean.thomas.trademe.listings.ListingsPresenter
import com.sean.thomas.trademe.network.Repository
import com.sean.thomas.trademe.network.models.Listing
import com.sean.thomas.trademe.util.TestSchedulersProvider
import io.reactivex.Flowable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ListingsPresenterUnitTest {

    @Mock
    private lateinit var view: ListingsContract.View

    @Mock
    private lateinit var repository: Repository

    private lateinit var presenter: ListingsContract.Presenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        // set up presenter
        presenter = ListingsPresenter(view, repository,  TestSchedulersProvider)
    }

    @After
    fun tearDown() {}

    @Test
    fun test_getListings_setsListingsIfValidResults() {
        // arrange
        val catId = "1234-"
        val results = listOf(Listing("listingId", "NA","href"))
        Mockito.`when`(repository.getListings(catId)).thenReturn(Flowable.just(results))

        // act
        (presenter as ListingsPresenter).getListings(catId, "")

        // assert
        verify(view, times(1)).showProgress()
        verify(view, times(1)).hideProgress()
        verify(view, times(1)).hideEmptyScreen()
        verify(view, times(1)).setListings(results)
    }

    @Test
    fun test_getListings_showsEmptyScreenIfEmptyResults() {
        // arrange
        val catId = "1234-"
        val catName = "Gardening"
        Mockito.`when`(repository.getListings(catId)).thenReturn(Flowable.just(listOf()))

        // act
        (presenter as ListingsPresenter).getListings(catId, catName)

        // assert
        verify(view, times(1)).showProgress()
        verify(view, times(1)).hideProgress()
        verify(view, times(1)).showEmptyScreen(catName)
    }

    @Test
    fun test_getListings_showsNetworkErrorMessageAfterFailure() {
        // arrange
        Mockito.`when`(repository.getListings("")).thenReturn(Flowable.error(Throwable()))

        // act
        (presenter as ListingsPresenter).getListings("", "")

        // assert
        verify(view, times(1)).showProgress()
        verify(view, times(1)).hideProgress()
        verify(view, times(1)).showNetworkErrorMessage()
    }
}