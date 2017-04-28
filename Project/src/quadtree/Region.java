/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadtree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Andres
 */
public class Region extends Node {

	public static int BORDER_WIDTH = 1;
	
	private final Node[] children;
	private final float width;
	private final float height;
	private final float midPointX;
	private final float midPointY;
	private final int depth;

	public Region(int regionX, int regionY, int depth) {
		this.children = new Node[4];
		this.depth = depth;
		
		int maxWidth = (Handler.SCREEN_SIZE);
		int maxHeight = (Handler.SCREEN_SIZE);
		
		this.width = (float)(maxWidth/(Math.pow(2, depth)));
		this.height = (float)(maxHeight/(Math.pow(2, depth)));
		
		this.x = regionX*this.width;
		this.y = regionY*this.height;
		
		this.midPointX = this.x + (this.width/2);
		this.midPointY = this.y + (this.height/2);
	}

	@Override
	public String toString() {
		String start = "("+this.x+","+this.y+")";
		String finish = "("+(this.x+this.width)+","+(this.y+this.height)+")";
		return "Region"+start+" -> "+finish;
	}
	
	/**
	 * @return the description of children
	 */
	public String getChildrenDescription() {
		String description = "";
		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] == null) {
				description = description + "null";
			} else {
				description = description + getChildren()[i].toString();
			}
			if (i+1 == getChildren().length) {
				description = description + ".";
			} else {
				description = description + ", ";
			}
		}
		return description;
	}
	
	public void addChild(Point child) {
		int childNum = 0;
		
		if (child.getX() > this.getMidPointX()) {
			childNum++;
		}
		
		if (child.getY() > this.getMidPointY()) {
			childNum++;
			childNum++;
		}
		
		if (getChildren()[childNum] == null) {
			children[childNum] = child;
		} else {
			if (getChildren()[childNum] instanceof Point) {
				Point oldChild = (Point)getChildren()[childNum];
				int regionX = (int)((childNum%2) + (this.x*2/this.getWidth()));
				int regionY = (int)((childNum/2) + (this.y*2/this.getHeight()));

				children[childNum] = new Region(regionX, regionY, getDepth()+1);
				((Region) getChildren()[childNum]).addChild(oldChild);
			}
			((Region) getChildren()[childNum]).addChild(child);
		}
	}
	
	@Override
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage((int)this.getWidth(), (int)this.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		g.setColor(Color.white);
		g.fillRect(0,0, (int)this.getWidth(), (int)this.getHeight());
		
		g.setColor(Color.black);
		int finalWidth = (int)(this.getWidth()-(BORDER_WIDTH*2));
		int finalHeight = (int)(this.getWidth()-(BORDER_WIDTH*2));
		g.fillRect(BORDER_WIDTH,BORDER_WIDTH,finalWidth,finalHeight);
		
		for (int i = 0; i < getChildren().length; i++) {
			if (getChildren()[i] != null) {
				int x = (int)(getChildren()[i].getX()-this.getX());
				int y = (int)(getChildren()[i].getY()-this.getY());
				g.drawImage(getChildren()[i].getImage(),x,y,null);
			}
		}
		
		return image;		
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @return the children
	 */
	public Node[] getChildren() {
		return children;
	}

	/**
	 * @return the midPointX
	 */
	public float getMidPointX() {
		return midPointX;
	}

	/**
	 * @return the midPointY
	 */
	public float getMidPointY() {
		return midPointY;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}
	
}