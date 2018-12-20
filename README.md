
# Installation

```
Download Source from git hub

- Download and Install Android Studio and SDK
	- Download : https://developer.android.com/studio/index.html
	- Installation : https://developer.android.com/studio/install.html


- Open Android Studio latest version

- Open Android Studio latest version

- Click on "Open an existing Android Studio project"
	- Select downlaoded project folder path.

- Unit Test Folder
	- app -> src -> androidTest

- Demo App Folder
	- app

- Library folder
	- tvpage-library
```

# Widget Details

#### 1. TvPageCarousellWidget
#### 2. TvPageSidebarWidget
#### 3. TvPageSoloWidget
#### 4. TvPageGalleryActivity

# 1.TvPageSideBarVideoWidget Usage

```javascript

XML Declaration

    <fragment
        android:id="@+id/tvPageSideBarWidget"
        android:name="com.tvpage.lib.view.TvPageSideBarVideoWidget"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:tvpw_modal_background_color="@color/gray"
        />

GET VIEW IN JAVA FILE

//get view from xml
TvPageSideBarVideoWidget widget = (TvPageSideBarVideoWidget) getSupportFragmentManager()
							 .findFragmentById(R.id.tvPageSideBarWidget);

//add customization options
widget.setModalBackGroundColor(getResources().getColor(R.color.colorPrimary));


 ```

 ```javascript
 JAVA Declaration			

 //Create Object of widget
 TvPageSideBarVideoWidget widget = new TvPageSideBarVideoWidget();

 //add customization options
 widget.setItemPlayButtonBackgroundColor(getResources().getColor(R.color.colorPrimary));

 //load widget fragment in activity
 FragmentManager fragmentManager = getSupportFragmentManager();
 android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
 fragmentTransaction.add(R.id.frmLayout, widget, widget.getClass().getCanonicalName());
 fragmentTransaction.commitAllowingStateLoss();

   ```

# TvPageSoloWidget Usage

```javascript

  XML Declaration			

 <fragment
    	android:id="@+id/tvPageSoloView"
    	android:name="com.tvpage.lib.view.TvPageSoloWidget"
    	android:layout_width="match_parent"
    	android:layout_height="match_parent"
  	  app:tvpw_modal_background_color="@color/gray"
  />

  GET VIEW IN JAVA FILE
  //get view from xml
  TvPageSoloWidget widget = (TvPageSoloWidget) getSupportFragmentManager()
	  							 .findFragmentById(R.id.tvPageSoloView);

  //add customization options
  widget.setModalBackGroundColor(getResources().getColor(R.color.colorPrimary));

```
```javascript

  JAVA Declaration			

  //create object of widget
  TvPageSoloWidget widget = new TvPageSoloWidget();

  //add customization options
  widget.setItemPlayButtonBackgroundColor(getResources().getColor(R.color.colorPrimary));

  //load widget fragment in activity
  FragmentManager fragmentManager = getSupportFragmentManager();
  android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
  fragmentTransaction.add(R.id.frmLayout, widget, widget.getClass().getCanonicalName());
  fragmentTransaction.commitAllowingStateLoss();

```

# TvPageCarousellWidget Usage

```javascript

 XML Declaration

 <fragment
        android:id="@+id/tvPageLatestVideoCarousel"
        android:name="com.tvpage.lib.view.TvPageCarouselVideoWidget"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:tvpw_solo_modal_border_color="@color/black"
       />

  GET VIEW IN JAVA FILE
  //get view from xml
  TvPageCarouselVideoWidget widget = (TvPageCarouselVideoWidget) getSupportFragmentManager()
		 	  							 .findFragmentById(R.id.tvPageLatestVideoCarousel);

  //add customization options
	widget.setModalBorderColor(getResources().getColor(R.color.colorPrimary));

```
```javascript

 JAVA Declaration			

 //create object of widget
 TvPageCarouselVideoWidget widget = new TvPageCarouselVideoWidget();

 //add customization options
 widget.setAutoVideoNext(false);

 //load widget fragment in activity
 FragmentManager fragmentManager = getSupportFragmentManager();
 android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
 fragmentTransaction.add(R.id.frmLayout, widget, widget.getClass().getCanonicalName());
 fragmentTransaction.commitAllowingStateLoss();

```

# TvPageGalleryActivity Usage

```javascript
TvPageGalleryActivity.getInstance()
         .setChannelId(this,"ENTER CHANNEL ID")
         .startActivity(this);

```
