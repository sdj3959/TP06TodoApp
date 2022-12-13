package com.sdj2022.tp06todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.sdj2022.tp06todoapp.TodoActivity
import android.content.SharedPreferences
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.view.View
import com.bumptech.glide.Glide
import com.sdj2022.tp06todoapp.R
import com.sdj2022.tp06todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.includeCategoryAll.root.setOnClickListener { v: View? -> clickCategory(0) }
        binding.includeCategoryWork.root.setOnClickListener { v: View? -> clickCategory(1) }
        binding.includeCategoryStudy.root.setOnClickListener { v: View? -> clickCategory(2) }
        binding.includeCategoryHealth.root.setOnClickListener { v: View? -> clickCategory(3) }
        binding.includeCategoryHobby.root.setOnClickListener { v: View? -> clickCategory(4) }
        binding.includeCategoryMeeting.root.setOnClickListener { v: View? -> clickCategory(5) }
        binding.includeCategoryEtc.root.setOnClickListener { v: View? -> clickCategory(6) }
        binding.includeCategoryDone.root.setOnClickListener { v: View? -> clickCategory(7) }
        loadData() //SharedPreferences 에 저장된 프로필이미지, 이름을 가져와서 뷰에 보여주는 기능호출
    }

    fun clickCategory(category: Int) {
        val intent = Intent(this, TodoActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    fun loadData() {
        // 설정한 프로필과 닉네임을 저장한 SharedPreferences "account" 에서 데이터 가져오기
        val pref = getSharedPreferences("account", MODE_PRIVATE)
        val profile = pref.getString("profile", "")
        val name = pref.getString("name", "")
        binding!!.tvName.text = "안녕하세요. $name 님"
        Glide.with(this).load(profile).error(R.drawable.user).into(binding!!.civProfile)
    }

    override fun onResume() {
        super.onResume()

        loadDatabaseAndUiUpdate()
    }

    private fun loadDatabaseAndUiUpdate(){
        // Database "Todo.db" 파일 안에 있는 todo 테이블의 카테고리별 개수 가져오기

        val db:SQLiteDatabase = openOrCreateDatabase("Todo", MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS todo(num INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, date TEXT, category INTEGER, note TEXT, isDone INTEGER)")

        var countAll:Long = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM todo WHERE isDone=0", null)
        var countWork:Long = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM todo WHERE isDone=0 and category=?", arrayOf("1"))
        var countStudy:Long = DatabaseUtils.longForQuery(db,"SELECT COUNT(*) FROM todo WHERE isDone=0 and category=?", arrayOf("2"))
        var countHealth:Long = DatabaseUtils.longForQuery(db,"SELECT COUNT(*) FROM todo WHERE isDone=0 and category=?", arrayOf("3"))
        var countHobby:Long = DatabaseUtils.longForQuery(db,"SELECT COUNT(*) FROM todo WHERE isDone=0 and category=?", arrayOf("4"))
        var countMeeting:Long = DatabaseUtils.longForQuery(db,"SELECT COUNT(*) FROM todo WHERE isDone=0 and category=?", arrayOf("5"))
        var countEtc:Long = DatabaseUtils.longForQuery(db,"SELECT COUNT(*) FROM todo WHERE isDone=0 and category=?", arrayOf("6"))
        var countDone:Long = DatabaseUtils.longForQuery(db,"SELECT COUNT(*) FROM todo WHERE isDone=1", null)

        binding.includeCategoryAll.tvNum.text = countAll.toString()
        binding.includeCategoryWork.tvNum.text = countWork.toString()
        binding.includeCategoryStudy.tvNum.text = countStudy.toString()
        binding.includeCategoryHealth.tvNum.text = countHealth.toString()
        binding.includeCategoryHobby.tvNum.text = countHobby.toString()
        binding.includeCategoryMeeting.tvNum.text = countMeeting.toString()
        binding.includeCategoryEtc.tvNum.text = countEtc.toString()
        binding.includeCategoryDone.tvNum.text = countDone.toString()
    }
}