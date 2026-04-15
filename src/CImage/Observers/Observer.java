package CImage.Observers;

import CImage.*;

public interface Observer 
{
    void setCImage(CImage ci);
    CImage getCImage();
    void update();
}
