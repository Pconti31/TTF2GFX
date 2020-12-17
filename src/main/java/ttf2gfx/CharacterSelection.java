package ttf2gfx;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * The Class CharacterSelection.
 * Supports Drag and Drop of CharacterHelper
 * 
 * @author Paul Conti
 * 
 */
public class CharacterSelection implements Transferable {

  /** The CharacterSelection flavor. */
  public static DataFlavor characterFlavor = new DataFlavor(CharacterHelper.class, "Character flavor");
  
  /** The selection. */
  private CharacterHelper selection;

  /**
   * Instantiates a new Character selection.
   *
   * @param selection
   *          the selection
   */
  public CharacterSelection(CharacterHelper selection){
     this.selection = selection;
  }

  // Transferable implementation

  /**
   * getTransferDataFlavors
   *
   * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
   */
  @Override
  public DataFlavor[] getTransferDataFlavors(){
//     System.out.println("getTransferDataFlavors");
     DataFlavor[] ret = {characterFlavor};
     return ret;
  }

  /**
   * isDataFlavorSupported
   *
   * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
   */
  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor){
     return characterFlavor.equals(flavor);
  }

  /**
   * getTransferData
   *
   * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
   */
  @Override
  public synchronized Object getTransferData (DataFlavor flavor)
     throws UnsupportedFlavorException 
  {
     if (isDataFlavorSupported(flavor)){
        return this.selection;
     } else {
        throw new UnsupportedFlavorException(characterFlavor);
     }
  }

}
