# File Browser
Implementation of a GUI File Browser in Swing

## Classes Description
1. **FileBrowser.java**: Main (Entry Point of Code to Render GUI)
2. **GlobalFrame.java**: Creates JFrame (Window), Menu Bar (Bar on Top), Favourites Panel (Panel to Render Favourites) and General Container (Search Bar, Breadcrumb and Content)
3. **MenuContainer.java**: Helper Class to Render Menu Items (Both for Bar on Top and for Pop-Up Menu)
4. **MenuBar.java**: Render Top Bar
5. **MenuPopUp.java**: Render Pop-Up Menu on Right Click
6. **FavouritesContainerPanel.java**: A Panel to Contain Favourites Labels
7. **FavouritesLabel.java**: Labels that are Rendered in the Favourites Container Panel
8. **SearchBarPanel.java**: Form and Search Button of the Search Bar
9. **SearchLabel.java**: Rendered Results that were Found with the Search Bar and Displayed in Content Container
10. **BreadcrumbPanel.java**: A Panel that Splits the Path and Renders Each individual File as a PathLink
11. **ContentPanel.java**: A Panel to Contain Contents of the Current Location on the System File
12. **ContentLabel.java**: Each File Represented as an Icon and a Name
13. **FileUtils.java**: A Helper Class for Basic File Functions (Copy-Paste, Delete etc) 
14. **GeneralContainer.java**: Search Bar(Visible or Invisible), Breadcrumb, Content
