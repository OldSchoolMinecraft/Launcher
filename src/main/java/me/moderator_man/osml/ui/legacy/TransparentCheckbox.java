/*    */ package me.moderator_man.osml.ui.legacy;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.JCheckBox;
/*    */ 
/*    */ public class TransparentCheckbox extends JCheckBox
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public TransparentCheckbox(String string)
/*    */   {
/* 11 */     super(string);
/* 12 */     setForeground(Color.WHITE);
/*    */   }
/*    */ 
/*    */   public boolean isOpaque() {
/* 16 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Users\camas\Downloads\Minecraft Launcher\
 * Qualified Name:     net.minecraft.TransparentCheckbox
 * JD-Core Version:    0.6.0
 */