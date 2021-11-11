package com.cactusknights.chefbook.legacy.migration

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.models.Recipe
import java.util.*
import kotlin.collections.ArrayList

const val DATABASE_NAME = "ChefBookDB"
const val DATABASE_VERSION = 3
const val TABLE_NAME = "Recipes"
const val TABLE_NAME_2 = "Categories"
const val COL_TITLE = "title"
const val COL_FAVOURITE = "favourite"
const val COL_AMOUNT = "amount"
const val COL_TIME = "time"
const val COL_CATEGORIES = "categories"
const val COL_INGREDIENTS = "ingredients"
const val COL_COOKING = "cooking"
const val COL_ID = "id"
const val COL_POPULARITY = "popularity"
const val COL_COUNT = "count"
const val COL_CALORICITY = "caloricity"

open class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        var createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " VARCHAR(256), " +
                COL_AMOUNT + " INTEGER, " +
                COL_TIME + " INTEGER, " +
                COL_CATEGORIES + " TEXT, " +
                COL_INGREDIENTS + " TEXT, " +
                COL_COOKING + " TEXT, " +
                COL_FAVOURITE + " INTEGER, " +
                COL_POPULARITY + " INTEGER, " +
                COL_CALORICITY + " INTEGER)"
        db?.execSQL(createTable)
        createTable = "CREATE TABLE " + TABLE_NAME_2 + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " VARCHAR(256), " +
                COL_COUNT + " INT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1) {
            val updateTable = "ALTER TABLE $TABLE_NAME ADD $COL_POPULARITY INT"
            db?.execSQL(updateTable)
            val insertPopularity = "UPDATE $TABLE_NAME SET $COL_POPULARITY = 0"
            db?.execSQL(insertPopularity)
            val updateCategoriesTable = "ALTER TABLE $TABLE_NAME_2 ADD $COL_COUNT INT"
            db?.execSQL(updateCategoriesTable)
            var query = "Select * FROM $TABLE_NAME_2"
            val result = db?.rawQuery(query, null)
            if(result!!.moveToFirst()) {
                do {
                    val category : String = result.getString(result.getColumnIndexOrThrow(COL_TITLE))
                    var count = 0
                    query = "Select * FROM $TABLE_NAME WHERE $COL_CATEGORIES LIKE '%$category%'"
                    val secondResult = db.rawQuery(query, null)
                    if(secondResult!!.moveToFirst()) {
                        do {
                            count += 1
                        } while(secondResult.moveToNext())
                    }
                    secondResult.close()
                    val cv = ContentValues()
                    cv.put(COL_COUNT, count)
                    db.update(TABLE_NAME_2, cv, "$COL_TITLE = '$category'", null)
                } while(result.moveToNext())
            }
            result.close()
        } else if (oldVersion == 2) {
            db?.execSQL("ALTER TABLE $TABLE_NAME ADD $COL_CALORICITY INT")
            db?.execSQL("UPDATE $TABLE_NAME SET $COL_CALORICITY = 0")
        }
    }

    open fun getData() : ArrayList<Recipe> {
        val allRecipes : ArrayList<Recipe> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()) {
            do {
                val categories = arrayListOf<String>()
                categories.addAll(result.getString(result.getColumnIndexOrThrow(COL_CATEGORIES)).split("@"))
                val cooking = arrayListOf<Selectable<String>>()
                cooking.addAll(result.getString(result.getColumnIndexOrThrow(COL_COOKING)).split("@").map { Selectable(it) })
                val recipe = Recipe(
                    id = 0, name = result.getString(result.getColumnIndexOrThrow(COL_TITLE)).toString(),
                    isFavourite = result.getString(result.getColumnIndexOrThrow(COL_FAVOURITE)).toInt().toBooleanImp(),
                    creationTimestamp = Date(),
                    categories =  categories,

                    servings = result.getString(result.getColumnIndexOrThrow(COL_AMOUNT)).toInt(),
                    time = result.getInt(result.getColumnIndexOrThrow(COL_TIME)),
                    calories = result.getString(result.getColumnIndexOrThrow(COL_CALORICITY)).toInt(),

                    ingredients = getIngredientBySql(result.getString(result.getColumnIndexOrThrow(COL_INGREDIENTS))),
                    cooking = cooking)
                allRecipes.add(recipe)
            } while(result.moveToNext())
        }
        result.close()
        return allRecipes
    }

    private fun Int.toBooleanImp(): Boolean {
        return this == 1
    }

    companion object {
        private fun getIngredientBySql(ingredientsSQL: String): ArrayList<Selectable<String>> {

            val ingredients = ingredientsSQL.split("^")
            val readyIngredients = arrayListOf<Selectable<String>>()

            for(currentIngredient in ingredients) {
                val metaData = currentIngredient.split("&").toTypedArray()
                if (metaData.size == 2) {
                    if (metaData[0].lowercase() != "uncategorized") readyIngredients.add(Selectable(metaData[0], true))
                    val currentIngredients = metaData[1].split("|").toTypedArray()
                    for (ingredient in currentIngredients) {
                        readyIngredients.add(Selectable(ingredient.replace("#", " - ")))
                    }
                } else if (metaData.size != 2) {
                    for (data in metaData) {
                        val things = data.split("|")
                        for (thing in things) {
                            if (thing.lowercase() != "uncategorized")
                                readyIngredients.add(Selectable(thing.replace("#", " - ")))
                        }
                    }
                }
            }
            return readyIngredients
        }
    }
}