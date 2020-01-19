# SingleRowCalendar

### The best and the most customizable single row calendar for Android !
* build on top of new patterns for example DiffCallback, SelectionTracker and RecyclerView
  


## Features
* you can choose between SINGLE or MULTIPLE SELECTION, also if you want you can set LONG PRESS TO START SELECTION
* you can easily set count of dates in past or future, or you can provide your own
* you can set initial position in your list
* you can choose between three types of displaying days, for example F,Fri,Friday
* you can provide different item layout fo weekends
* you can use up to three special items in calendar
* you can easily observe for changes in selection
* you can programatically deselect and select items
* you can check if item is selected, set selection and clear selection

## Usage

#### 1. CREATE XML CALENDAR ITEM FILE
*   It's file which defining look of your single item in calendar
   //TODO IMAGE
   
#### 2. CREATE XML SELECTED CALENDAR ITEM FILE
   * It's file which defining look of your selected item in calendar
   //TODO IMAGE

#### 3. ADD SINGLE ROW CALENDAR TO YOUR LAYOUT FILE

```xml
<com.michalsvec.singlerowcalendar.SingleRowCalendar
        android:id="@+id/main_single_row_calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:itemLayoutId="@layout/calendar_item"
        app:selectedItemLayoutId="@layout/selected_calendar_item"
        app:dateTextViewId="@id/tv_date_calendar_item"
        app:dayTextViewId="@id/tv_day_abbreviation_calendar_item"
        app:futureDaysCount="30">
```

#### 4. INIT SINGLE ROW CALENDAR IN YOUR ACTIVITY OR FRAGMENT FILE

```kotlin
val singleRowCalendar = main_single_row_calendar as SingleRowCalendar
singleRowCalendar.apply {
    init()
}
```

#### 5. SETUP OBSERVER FOR CHANGES

```kotlin
   singleRowCalendar.setCalendarChangesObserver(object : CalendarChangesObserver {
            override fun whenMonthAndYearChanged(monthNumber: String, monthName: String, year: String) {
                super.whenMonthAndYearChanged(monthNumber, monthName, year)
            }

            override fun whenSelectionChanged(isSelected: Boolean, index: Int, date: Date) {
                super.whenSelectionChanged(isSelected, index, date)
            }
        })
```

## Customization and more attributes

* ```itemLayoutId```
  * reference for basic calendar item layout
* ```dateTextViewId```
  * id of TextView which displaying date in calendar item
* ```dayTextViewId```
  * id of TextView which displaying day abbrevation in calendar item
* ```monthTextViewId```
  * id of TextView which displaying month in calendar item
* ```selectedItemLayoutId```
  * reference for basic selected calendar item layout    
* ```pastDaysCount```
  * number of days in past displayed in calednar
* ```futureDaysCount```
  * number of days in future displayed in calednar
* ```includeCurrentDate```
  * include current date in calendar
* ```initialPositionIndex```
  * first displayed item in calendar
* ```dayNameFormat```
  * format of displaying day 
  * three possible values 
    * threeLetters - for example Fri, Mon
    * oneLetter - for example F, M
    * fullName - for example Friday, Monday
* ```multiSelection```
  * enable or disable multi selection
* ```disableUnselection```
  * disable unselection 
* ```enableLongPress```
  * first selected item starts with long press
* ```weekendItemLayoutId```
  * reference for weekend calendar item layout which is displayed at weekends 
* ```weekendSelectedItemLayoutId```
  * reference for weekend selected calendar item layout which is displayed at weekends 
* ```firstSpecialItemLayoutId```
  * reference for first special calendar item layout which is according ```firstSpecialItemPositionList```
* ```firstSelectedSpecialItemLayoutId```
  * reference for first special selected calendar item layout which is displayed when first special item is selected
* ```secondSpecialItemLayoutId```
  * reference for second special calendar item layout which is according ```secondSpecialItemPositionList```
* ```secondSelectedSpecialItemLayoutId```
  * reference for second special selected calendar item layout which is displayed when second special item is selected
* ```thirdSpecialItemLayoutId```
  * reference for third special calendar item layout which is according ```thirdSpecialItemPositionList```
* ```thirdSelectedSpecialItemLayoutId```
  * reference for third special selected calendar item layout which is displayed when third special item is selected
* ```firstSpecialItemPositionList```
  * positions which are defining where ```firstSpecialItemLayoutId``` and ```firstSelectedSpecialItemLayoutId``` will be displayed
* ```secondSpecialItemPositionList```
  * positions which are defining where ```secondSpecialItemLayoutId``` and ```secondSelectedSpecialItemLayoutId``` will be displayed
* ```thirdSpecialItemPositionList```
  * position which are defining where ```thirdSpecialItemLayoutId``` and ```thirdSelectedSpecialItemLayoutId``` will be displayed



        

        
        
        
        
        
        
       
        
