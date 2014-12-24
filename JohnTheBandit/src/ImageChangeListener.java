/**
 * Created by needa_000 on 12/23/2014.
 */
public interface ImageChangeListener
{
    public abstract void onChangeLocation(OnScreenObject changedObject, int oldX, int oldY);
    public abstract void onChangeImage(OnScreenObject changedObject);
}
