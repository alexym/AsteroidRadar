package com.udacity.asteroidradar.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.ApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as ARListAdapter
    adapter.submitList(data) {
        recyclerView.scrollToPosition(0)
    }
}

@BindingAdapter("imageOftheDay")
fun bindImage(imgView: ImageView, asteroidImg: PictureOfDay?) {
    asteroidImg?.let {
        Picasso.get().load(asteroidImg.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(imgView)
        imgView.contentDescription = imgView.context.getString(R.string.nasa_picture_of_day_content_description_format)
    }
}

@BindingAdapter("titleImg")
fun bindImage(textView: TextView, asteroidImg: PictureOfDay?) {
    asteroidImg?.let {
        val context = textView.context
        textView.text = String.format(context.getString(R.string.image_of_the_day_title), asteroidImg.title)
        textView.contentDescription = String.format(context.getString(R.string.image_of_the_day_title), asteroidImg.title)
    }
}

@BindingAdapter("progressStatus")
fun bindStatus(statusProgressB: ProgressBar, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusProgressB.visibility = View.VISIBLE
        }
        ApiStatus.ERROR -> {
            statusProgressB.visibility = View.GONE
        }
        ApiStatus.DONE -> {
            statusProgressB.visibility = View.GONE
        }
    }
}

