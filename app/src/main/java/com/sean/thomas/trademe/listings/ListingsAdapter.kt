package com.sean.thomas.trademe.listings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sean.thomas.trademe.R
import com.sean.thomas.trademe.network.models.Listing
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_listing.view.*

/**
 * Populates a listings recycler view.
 */
class ListingsAdapter (
    private val onListingClicked: (listing: Listing) -> Unit
    ): RecyclerView.Adapter<ListingsAdapter.ListingViewHolder>() {

    private var listings: List<Listing> = ArrayList()

    class ListingViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val thumbnail: ImageView = v.thumbnail
        val title: TextView = v.title
        val listingId: TextView = v.listing_id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        return ListingViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_listing, parent, false))
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val listing = listings[position]

        Picasso.with(holder.itemView.context)
                .load(listing.thumbUrl)
                .into(holder.thumbnail);

        holder.title.text = listing.title
        holder.listingId.text = listing.listingId

        holder.itemView.setOnClickListener({onListingClicked(listing)})
    }

    fun setListings(listings: List<Listing>) {
        this.listings = listings
        notifyDataSetChanged()
    }
}