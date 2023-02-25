package com.bysafmobile.yumfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bysafmobile.yumfood.databinding.PopularItemsBinding
import com.bysafmobile.yumfood.pojo.CategoryMeals

class MostPopularAdapter:RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    // инициализируем переменную mealsList
    private var mealsList = ArrayList<CategoryMeals>()
    lateinit var onItemClick:((CategoryMeals) -> Unit)

    // каждый раз когда нужно установить новый список
    // мы обновляем наш адаптер, когда обновились данные во view
    fun setMeals(mealsList:ArrayList<CategoryMeals>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    // метод для создания ViewHolder
    // возвращаем объект класса PopularMealViewHolder в качестве шаблона
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    // заполняем картинками View
    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)
        // добавим слушатель нажатий в RecyclerView
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
    }

    // адаптер должен знать количество элементов в RecyclerView
    // которые нужно отобразить
    override fun getItemCount(): Int {
        return mealsList.size
    }
    // этот класс создается для каждого элемента списка и отрисовывает его
    class PopularMealViewHolder(var binding: PopularItemsBinding):RecyclerView.ViewHolder(binding.root)
}