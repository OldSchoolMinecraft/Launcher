/*    */ package me.moderator_man.osml.ui.legacy;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.JLabel;
/*    */ 
/*    */ public class TransparentLabel extends JLabel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public TransparentLabel(String string, int center)
/*    */   {
/* 11 */     super(string, center);
/* 12 */     setForeground(Color.WHITE);
/*    */   }
/*    */ 
/*    */   public TransparentLabel(String string) {
/* 16 */     super(string);
/* 17 */     setForeground(Color.WHITE);
/*    */   }
/*    */ 
/*    */   public boolean isOpaque() {
/* 21 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Users\camas\Downloads\Minecraft Launcher\
 * Qualified Name:     net.minecraft.TransparentLabel
 * JD-Core Version:    0.6.0
 */