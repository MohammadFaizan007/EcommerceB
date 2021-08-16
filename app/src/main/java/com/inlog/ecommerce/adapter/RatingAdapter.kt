package com.inlog.ecommerce.adapter

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.inlog.ecommerce.R
import com.inlog.ecommerce.model.RatingViewModel
import com.inlog.ecommerce.util.ReadMoreOption


class RatingAdapter(val ratingList: ArrayList<RatingViewModel>, val isForProduct: Boolean, val context: Context?) : RecyclerView.Adapter<RatingAdapter.MyViewHolder>() {
    private val readMoreOption: ReadMoreOption
    class MyViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {

        var ratingBarId: com.chaek.android.RatingBar?
        var txtVName: TextView?
         var txtVDate: TextView?
         var txtVDescription: TextView
         var imgViewMore: ImageView?

        init {
            txtVName = viewItem.findViewById<TextView>(R.id.txtVName)
            txtVDate = viewItem.findViewById<TextView>(R.id.txtVDate)
            txtVDescription = viewItem.findViewById<TextView>(R.id.txtVDescription)
            ratingBarId = viewItem.findViewById(R.id.ratingBarId)
            imgViewMore = viewItem.findViewById(R.id.imgViewMore)

        }
    }
    init {
        readMoreOption = ReadMoreOption.Builder(context)
                .build()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_rating, null))

    }

    override fun getItemCount(): Int {
        return ratingList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val obj = ratingList[position]
        holder.txtVName?.text = obj.name
        holder.txtVDate?.text = obj.create_date
//        holder.txtVDescription.text = obj.description
//        if (position % 2 == 0) {
//            readMoreOption.addReadMoreTo(
//                    holder.txtVDescription,
//                    obj.description
//            )
//        } else {
//        if (position == 4)
//        {
//            readMoreOption.addReadMoreTo(
//                    holder.txtVDescription,
//                    "US President Joe Biden’s administration wants to create a US\$6.5-billion agency to accelerate innovations in health and medicine — and revealed new details about the unit last month1. Dubbed ARPA-Health (ARPA-H), it is the latest in a line of global science agencies now being modelled on the renowned US Defense Advanced Research Projects Agency (DARPA), whose work a generation ago laid the foundation for the modern Internet.\n" +
//                            "\n" +
//                            "With more DARPA clones on the horizon, researchers warn that success in replicating DARPA’s hands-on, high-risk, high-reward approach is by no means assured."
//            )
//        }
//        else
//        {
            readMoreOption.addReadMoreTo(
                    holder.txtVDescription,
                    obj.description,


            )
//        }

//        }
        holder.ratingBarId?.setScore(obj.avg_rating.toFloat())
        holder.imgViewMore?.setOnClickListener {
            val dialog = holder.imgViewMore?.context?.let { it1 -> Dialog(it1) }
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(true)
            dialog?.setContentView(R.layout.rating_dialog)
            val delivery :com.chaek.android.RatingBar? = dialog?.findViewById(R.id.ratingBarDelivery)
            val raingProfesional :com.chaek.android.RatingBar? = dialog?.findViewById(R.id.raingProfesional)
            val goodQuality :com.chaek.android.RatingBar? = dialog?.findViewById(R.id.goodQuality)
            val ratingResponsive :com.chaek.android.RatingBar? = dialog?.findViewById(R.id.ratingResponsive)
            val txtV1 :TextView?= dialog?.findViewById(R.id.txtV1)
            val txtV2 :TextView?= dialog?.findViewById(R.id.txtV2)
            val txtV3 :TextView?= dialog?.findViewById(R.id.txtV3)
            val rlResponsiveId :RelativeLayout?= dialog?.findViewById(R.id.rlResponsiveid)
            if(isForProduct){
                delivery?.setScore(obj.value_for_money_rating.toFloat())
                raingProfesional?.setScore(obj.quality_rating.toFloat())
                goodQuality?.setScore(obj.meet_expectation_rating.toFloat())
//                ratingResponsive?.setScore(obj.responsive_rating.toFloat())
                txtV1?.text = "Value for Money"
                txtV2?.text = "Quality"
                txtV3?.text = "Meet Expectation"
                rlResponsiveId?.visibility = View.GONE
            }else {
                delivery?.setScore(obj.delivery_rating.toFloat())
                raingProfesional?.setScore(obj.professional_rating.toFloat())
                goodQuality?.setScore(obj.good_quality_rating.toFloat())
                ratingResponsive?.setScore(obj.responsive_rating.toFloat())
                rlResponsiveId?.visibility = View.VISIBLE
            }
            dialog?.show()
        }

    }
}