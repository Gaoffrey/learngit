package com.ems.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ems.entity.Article;
import com.ems.entity.CheckRecord;
import com.ems.entity.IssueRecord;
import com.ems.entity.MxcbArticle;
import com.ems.entity.MxcbPicture;
import com.ems.entity.base.AbstractCheckRecord;
import com.ems.entity.base.AbstractIssueRecord;
import com.ems.manager.CheckRecordMng;
import com.ems.manager.IssueRecordMng;
import com.ems.wap.huayu.util.HtmlUtil;


/**
 *全文检索工具类
 */
public class SearchTextUtil{
	
	@Autowired
    private IssueRecordMng issueRecordMng;
	
	@Autowired
    private CheckRecordMng checkRecordMng;
	
	
	private static SearchTextUtil searchTextUtil;
	
	private  SearchTextUtil() {}
	
	public static synchronized SearchTextUtil getInstance() {     //返回一个单例的SearchTextUtil类的对象。
        if(searchTextUtil==null){
        	searchTextUtil=new SearchTextUtil();
        }
        return searchTextUtil;
    }

   //public HashMap<Integer,Integer> searchKeyWordsForAll(IssueRecord ir,CheckRecord cr,Article article){    //返回一个map对象。
   //public String searchKeyWordsForAll(IssueRecord ir,CheckRecord cr,Article article){    //返回一个map对象。
	
	public void searchKeyWordsForAll(IssueRecord ir,CheckRecord cr,Article article){    //什么都不返回。
		Long articleLxId = article.getArticleLx().getParentId() == 0 ? article.getArticleLx().getArticleLxId()
                : article.getArticleLx().getParentId();
		if(ir!=null){
			if(articleLxId>=1&&articleLxId<=4){
				searchKeyWordsForWords(ir,cr,article);
			}else if(articleLxId==5||articleLxId==6||articleLxId==13||articleLxId==16||articleLxId==7||articleLxId==18){
				searchKeyWordsForPicture(ir,cr,article);
			}else if(articleLxId==17){
				searchKeyWordsForTW(ir,cr,article);
			}
		}else if(cr!=null){
			if(articleLxId>=1&&articleLxId<=4){
				searchKeyWordsForWords(ir,cr,article);
			}else if(articleLxId==5||articleLxId==6||articleLxId==13||articleLxId==16||articleLxId==7||articleLxId==18){
				searchKeyWordsForPicture(ir,cr,article);
			}else if(articleLxId==17){
				searchKeyWordsForTW(ir,cr,article);
			}
		}else{
			if(articleLxId>=1&&articleLxId<=4){
				searchKeyWordsForWords(ir,cr,article);
			}else if(articleLxId==5||articleLxId==6||articleLxId==13||articleLxId==16||articleLxId==7||articleLxId==18){
				searchKeyWordsForPicture(ir,cr,article);
			}else if(articleLxId==17){
				searchKeyWordsForTW(ir,cr,article);
			}
		}
	}
	
   public void searchKeyWordsForWords(IssueRecord ir,CheckRecord cr,Article article){    //什么都不返回。
	   
	   Iterator iteratorList =loadProperties();
	   
	   int startPosition;
	
	   String articleContent ="";                            //清除页面元素，获取稿件内容。
	   try{
           if(ir!=null){
        	   //System.out.println("ir被调用");
        	   articleContent =ir.getIssueContent();
        	   while(iteratorList.hasNext()){
        		   String keyWordTemp =(String) iteratorList.next();
        		   if(articleContent!=null){
        			   startPosition=articleContent.indexOf(keyWordTemp);
            		   if(startPosition>-1){
            			   articleContent =articleContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
            			   ir.setIssueContent(articleContent);
            		   }
        		   }
        		   
        	   }
           }else if(cr!=null){
        	   //System.out.println("第一次cr被调用");
        	   articleContent =cr.getCheckContent();
        	   while(iteratorList.hasNext()){
        		   String keyWordTemp =(String) iteratorList.next();
        		   if(articleContent!=null){
        			   startPosition=articleContent.indexOf(keyWordTemp);
            		   if(startPosition>-1){
            			   articleContent =articleContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
            			   cr.setCheckContent(articleContent);
            		   }
        		   }
        		   
        	   }
           }else if(article!=null){
        	   //System.out.println("第一次article被调用");
        	   articleContent =article.getContent();
        	   while(iteratorList.hasNext()){
        		   String keyWordTemp =(String) iteratorList.next();
        		   if(articleContent!=null){
        			   startPosition=articleContent.indexOf(keyWordTemp);
            		   if(startPosition>-1){
            			   articleContent =articleContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
            			   article.setContent(articleContent);
            		   }
        		   }
        		   
        	   }
           }
		   //System.out.println(articleContent);
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	}
	    
	    
	 public void searchKeyWordsForPicture(IssueRecord ir,CheckRecord cr,Article article){    //什么都不返回。
		    
		    Iterator iteratorList =loadProperties();
		 	    
		   int startPosition;
		    
	 	   String articleContent ="";                            //清除页面元素，获取稿件内容。
	 	   try{
	            if(ir!=null){
	         	   //System.out.println("ir被调用");
	         	   
	         	   String[] imgDesArray =ArticleUtil.getIssueImgDesArray(ir);
	         	   
		 	       while(iteratorList.hasNext()){
		 	    	    String keyWordTemp =(String) iteratorList.next();
		 	    	
		 	    	    if(imgDesArray!=null){
		 	    	    	for(int i =0;i<imgDesArray.length;i++){
			 	    	    	if(imgDesArray[i]!=null){
			 	    	    		startPosition=imgDesArray[i].indexOf(keyWordTemp);
				 	    		    if(startPosition>-1){
				 	    			   articleContent =imgDesArray[i].replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
				 	    			   ArticleUtil.modifyIssueRecordImg(ir, i+1, articleContent);
				 	    		   }
			 	    	    	}
			 	    		    
			 	    	   }
		 	    	    }
		 	    	    
		 	       }
	         	   
	         	   
	            }else if(cr!=null){
	         	   //System.out.println("cr被调用");
	            	String[] imgDesArray =ArticleUtil.getCheckImgDesArray(cr);
			 	   
			 	       while(iteratorList.hasNext()){
			 	    	    String keyWordTemp =(String) iteratorList.next();
			 	    	    
			 	    	   if(imgDesArray!=null){
			 	    		  for(int i =0;i<imgDesArray.length;i++){
				 	    	    	if(imgDesArray[i]!=null){
				 	    	    		startPosition=imgDesArray[i].indexOf(keyWordTemp);
					 	    		    if(startPosition>-1){
					 	    			   articleContent =imgDesArray[i].replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
					 	    			   ArticleUtil.modifyCheckRecordImg(cr, i+1, articleContent);
					 	    		   }
				 	    	    	}
				 	    		    
				 	    	   }
			 	    	   }
			 	    	
			 	    	    
			 	       }
	            }else if(article!=null){
	         	   //System.out.println("article被调用");
	            	String[] imgDesArray =ArticleUtil.getImgDesArray(article);
		         	   
			 	       while(iteratorList.hasNext()){
			 	    	    String keyWordTemp =(String) iteratorList.next();
			 	    	
			 	    	   if(imgDesArray!=null){
			 	    		  for(int i =0;i<imgDesArray.length;i++){
				 	    	    	if(imgDesArray[i]!=null){
				 	    	    		startPosition=imgDesArray[i].indexOf(keyWordTemp);
					 	    		    if(startPosition>-1){
					 	    			   articleContent =imgDesArray[i].replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
					 	    			   ArticleUtil.modifyArticleImgDes(article, i+1, articleContent);
					 	    		   }
				 	    	    	}
				 	    		    
				 	    	   }
			 	    	   }
			 	    	    
			 	       }
	            }
	 		   //System.out.println(articleContent);
	 	   }catch(Exception e){
	 		   e.printStackTrace();
	 	   }
	 }
	    
	    
	    
	    
        public void searchKeyWordsForTW(IssueRecord ir,CheckRecord cr,Article article){    //什么都不返回。
		    
		    Iterator iteratorList =loadProperties();
		 	    
		   int startPosition;
		    
	 	   String articleContent ="";                            //接收稿件图说内容。
	 	   
	 	   String wordsContent ="";                              //接收稿件文字内容。
	 	   try{
	            if(ir!=null){
	         	   //System.out.println("ir被调用");
	         	   
	         	   String[] imgDesArray =ArticleUtil.getIssueImgDesArray(ir);
	         	   
	         	  wordsContent =ir.getIssueContent();
	         	   
		 	       while(iteratorList.hasNext()){
		 	    	    String keyWordTemp =(String) iteratorList.next();
		 	    	
		 	    	   if(imgDesArray!=null){
		 	    		  for(int i =0;i<imgDesArray.length;i++){
			 	    	    	if(imgDesArray[i]!=null){
			 	    	    		startPosition=imgDesArray[i].indexOf(keyWordTemp);
				 	    		    if(startPosition>-1){
				 	    			   articleContent =imgDesArray[i].replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
				 	    			   ArticleUtil.modifyIssueRecordImg(ir, i+1, articleContent);
				 	    		   }
			 	    	    	}
			 	    		    
			 	    	   }
		 	    	   }
		 	    	    
		 	    	    
		 	    	   if(wordsContent!=null){
	        			   startPosition=wordsContent.indexOf(keyWordTemp);
	            		   if(startPosition>-1){
	            			   wordsContent =wordsContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
	            			   ir.setIssueContent(wordsContent);
	            		   }
	        		   }
		 	       }
	         	   
	         	   
	            }else if(cr!=null){
	         	   //System.out.println("cr被调用");
	            	String[] imgDesArray =ArticleUtil.getCheckImgDesArray(cr);
	            	
	            	wordsContent =cr.getCheckContent();
			 	   
			 	       while(iteratorList.hasNext()){
			 	    	    String keyWordTemp =(String) iteratorList.next();
			 	    	
			 	    	   if(imgDesArray!=null){
			 	    		  for(int i =0;i<imgDesArray.length;i++){
				 	    	    	if(imgDesArray[i]!=null){
				 	    	    		startPosition=imgDesArray[i].indexOf(keyWordTemp);
					 	    		    if(startPosition>-1){
					 	    			   articleContent =imgDesArray[i].replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
					 	    			   ArticleUtil.modifyCheckRecordImg(cr, i+1, articleContent);
					 	    		   }
				 	    	    	}
				 	    		    
				 	    	   }
			 	    	   }
			 	    	    
			 	    	    
			 	    	   if(wordsContent!=null){
		        			   startPosition=wordsContent.indexOf(keyWordTemp);
		            		   if(startPosition>-1){
		            			   wordsContent =wordsContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
		            			   cr.setCheckContent(wordsContent);
		            		   }
		        		   }
			 	       }
	            }else if(article!=null){
	         	   //System.out.println("article被调用");
	            	String[] imgDesArray =ArticleUtil.getImgDesArray(article);
	            	
	            	wordsContent =article.getContent();
		         	   
			 	       while(iteratorList.hasNext()){
			 	    	    String keyWordTemp =(String) iteratorList.next();
			 	    	
			 	    	   if(imgDesArray!=null){
			 	    		  for(int i =0;i<imgDesArray.length;i++){
				 	    	    	if(imgDesArray[i]!=null){
				 	    	    		startPosition=imgDesArray[i].indexOf(keyWordTemp);
					 	    		    if(startPosition>-1){
					 	    			   articleContent =imgDesArray[i].replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
					 	    			   ArticleUtil.modifyArticleImgDes(article, i+1, articleContent);
					 	    		   }
				 	    	    	}
				 	    		    
				 	    	   }
			 	    	   }
			 	    	    
			 	    	    
			 	    	   if(wordsContent!=null){
		        			   startPosition=wordsContent.indexOf(keyWordTemp);
		            		   if(startPosition>-1){
		            			   wordsContent =wordsContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
		            			   article.setContent(wordsContent);
		            		   }
		        		   }
			 	       }
	            }
	 		   //System.out.println(articleContent);
	 	   }catch(Exception e){
	 		   e.printStackTrace();
	 	   }
	 }
	    
	    
	    
	    public Iterator loadProperties(){
	    	InputStream insKeyWord = this.getClass().getResourceAsStream("/keyword.properties");  //指定文件路径和文件名称。
	  	 	 Properties psKeyWord = new Properties();
	  	 	 try {
	  	 		psKeyWord.load(insKeyWord);    //加载有关键词的配置文件。
	  	 	} catch (IOException e) {
	  	 		e.printStackTrace();
	  	 	}finally{
	  	 		try {
	  	 			insKeyWord.close();                   //最后关闭输入流。
	  	 		} catch (IOException e) {
	  	 			e.printStackTrace();
	  	 		}
	  	 	}
	  	 	    
	  	 	String keyContent =psKeyWord.getProperty("keywords");      //获取配置文件中value的内容。
		 	    
		    List<String> keyWordList = Arrays.asList(keyContent.split(","));    //把配置文件里边的value转化成list。
		 	    
		    Iterator iteratorList =keyWordList.iterator();
		    
		    return iteratorList;
	    }
	    
	    
	    
	    
	    
	    
	    public void searchKeyWordsForMxcb(MxcbArticle ma,List<MxcbPicture> mpList){    //什么都不返回。
	    	
	    	/*Long articleLxId = article.getArticleLx().getParentId() == 0 ? article.getArticleLx().getArticleLxId()
	                : article.getArticleLx().getParentId();*/
	    	
	    	if(ma!=null){
	    		
	    		String lx = ma.getLx();
	    		
				if(lx.equals("编译")){
					searchKeyWordsForMxcbWords(ma);
				}else if(lx.equals("图组")||lx.equals("多媒体")){
					searchKeyWordsForMxcbPicture(mpList);
				}else if(lx.equals("文图")){
					searchKeyWordsForMxcbTW(ma,mpList);
				}
			}
		 	   
		 }
	    
	    
	    public void searchKeyWordsForMxcbWords(MxcbArticle ma){    //什么都不返回。
	    	
		       Iterator iteratorList =loadProperties();
		       
		       int startPosition;
		    	
		 	   String articleContent ="";                            //清除页面元素，获取稿件内容。
		 	   try{
		            if(ma!=null){
		         	   //System.out.println("ma被调用");
		         	   articleContent =ma.getContent();
		         	  while(iteratorList.hasNext()){
		         		 String keyWordTemp =(String) iteratorList.next();
		         		 if(articleContent!=null){
		         			startPosition=articleContent.indexOf(keyWordTemp);
			         		 if(startPosition>-1){
			         			articleContent =articleContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
			         			ma.setContent(articleContent);
			         		 }
		         		 }
		         		 
		         	  }
		            }
		 	   }catch(Exception e){
		 		   e.printStackTrace();
		 	   }
		 	   
		 	}
	    
	    
	    public void searchKeyWordsForMxcbPicture(List<MxcbPicture> mpList){    //什么都不返回。
	    	
	    	Iterator iteratorList =loadProperties();
	    	
	        int startPosition;
			    
		 	String articleContent ="";                            //清除页面元素，获取稿件内容。
		 	try{
		       if(mpList!=null){
		            
		            //System.out.println(mpList.size());
		            while(iteratorList.hasNext()){                            //关键词的迭代。
		            	String keyWordTemp =(String) iteratorList.next();
		            	if(mpList!=null){
		            		for(int i =0;i<mpList.size();i++){                //稿件内容的迭代。
			            		MxcbPicture mxcbPictureTemp =mpList.get(i);
			            		if(mxcbPictureTemp!=null){
			            			articleContent =mxcbPictureTemp.getIllustrate();
			            			if(articleContent!=null){
			            				startPosition =articleContent.indexOf(keyWordTemp);
			            				if(startPosition>-1){
			            					articleContent =articleContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
			            					//System.out.println(articleContent);
			            					mxcbPictureTemp.setIllustrate(articleContent);
			            				}
			            			}
			            		}
			            	}
		            	}
		            }
		       }
		   }catch(Exception e){
		 	   e.printStackTrace();
		    }
		 }
	    
	    
	    
        public void searchKeyWordsForMxcbTW(MxcbArticle ma,List<MxcbPicture> mpList){    //什么都不返回。
	    	
	    	Iterator iteratorList =loadProperties();
	    	
	    	Iterator iteratorListForWords =loadProperties();
	    	
	        int startPosition;
			    
		 	String articleContent ="";                            //清除页面元素，获取稿件内容。
		 	try{
		       if(mpList!=null){
		            
		            //System.out.println(mpList.size());
		            while(iteratorList.hasNext()){                            //关键词的迭代。
		            	String keyWordTemp =(String) iteratorList.next();
		            	if(mpList!=null){
		            		for(int i =0;i<mpList.size();i++){                //稿件内容的迭代。
			            		MxcbPicture mxcbPictureTemp =mpList.get(i);
			            		if(mxcbPictureTemp!=null){
			            			articleContent =mxcbPictureTemp.getIllustrate();
			            			if(articleContent!=null){
			            				startPosition =articleContent.indexOf(keyWordTemp);
			            				if(startPosition>-1){
			            					articleContent =articleContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
			            					//System.out.println(articleContent);
			            					mxcbPictureTemp.setIllustrate(articleContent);
			            				}
			            			}
			            		}
			            	}
		            	}
		            }
		       }
		       
		       if(ma!=null){
	         	   //System.out.println("ma被调用");
	         	   articleContent =ma.getContent();
	         	  while(iteratorListForWords.hasNext()){
	         		 String keyWordTemp =(String) iteratorListForWords.next();
	         		 if(articleContent!=null){
	         			startPosition=articleContent.indexOf(keyWordTemp);
		         		 if(startPosition>-1){
		         			articleContent =articleContent.replaceAll(keyWordTemp, "<label style='background-color:#FF6600;'>"+keyWordTemp+"</label>");
		         			//System.out.println(articleContent);
		         			ma.setContent(articleContent);
		         		 }
	         		 }
	         		 
	         	  }
	            }
		   }catch(Exception e){
		 	   e.printStackTrace();
		    }
		 }
	    
	    
}
