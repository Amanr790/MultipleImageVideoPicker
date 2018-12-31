# MULTIPLEMEDIAPICKER

MULTIPLEMEDIAPICKER is a library which  used to select multiple images or videos.

<b>Features:</b>
<ul>
  <li>Multiple Image Picker </li>
  <li>Multiple Video Picker</li>
</ul>

![MediaPicker gif](Media/mediapicker.gif)

<b>Getting started:</b>

<ul>
  <li>Add multiplemediapicker into your project as module.</li>
<li> Add below line under <b>dependencies{..}</b> tag of app.gradle of your project
<br>
<b>compile project(path: ':multiplemediapicker')</b></li>
</ul>

<b>Implementation:</b>

// selectedImages or selectedVideos are all already selected images or videos.

  <li>Fetch Multiple Images</li>


        Fetch - 
           private void openImageGallery(List<String> selectedImages)
          {
              Intent intent= new Intent(MediaActivity.this, ImagesGallery.class);
              intent.putExtra("selectedList", (Serializable) selectedImages);
              // Set the title
              intent.putExtra("title","Select Image");
              intent.putExtra("maxSelection",maxSelect); // Optional
              startActivityForResult(intent,MULTIPLE_IMAGE_INTENT);
          }
          
          Get - 
              
             if(requestCode==MULTIPLE_IMAGE_INTENT && data!=null)
            {
                if(data.getStringArrayListExtra("result")!=null) {
                    ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
                    for (int i = 0; i < selectionResult.size(); i++) {
                        MediaBean mediaBean = new MediaBean();
                        mediaBean.setMediaPath(selectionResult.get(i));
                        mediaBean.setMediaType(1);
                        mediaResult.add(mediaBean);
                    }
                    showMediaAdaptor();
                }
            }
                    
  <li>Fetch Multiple Videos</li>


           Fetch - 
            private void openVideoGallery(List<String> selectedVideos )
           {
               Intent intent= new Intent(MediaActivity.this, VideosGallery.class);
               intent.putExtra("selectedList", (Serializable) selectedVideos);
               // Set the title
               intent.putExtra("title","Select Video");
               intent.putExtra("maxSelection",maxSelect); // Optional
               startActivityForResult(intent,MULTIPLE_VIDEO_INTENT);
           }
              
             Get -  
             if(requestCode==MULTIPLE_VIDEO_INTENT && data!=null)
             {
                 if(data.getStringArrayListExtra("result")!=null) {
                     ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
                     for (int i = 0; i < selectionResult.size(); i++) {
                         MediaBean mediaBean = new MediaBean();
                         mediaBean.setMediaPath(selectionResult.get(i));
                         mediaBean.setMediaType(2);
                         mediaResult.add(mediaBean);
                     }
                     showMediaAdaptor();
                 }
             }
