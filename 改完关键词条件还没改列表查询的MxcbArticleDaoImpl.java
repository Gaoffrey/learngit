package com.ems.dao.impl;
 
import java.util.List;
import org.springframework.stereotype.Repository;

import com.ems.dao.MxcbArticleDao;
import com.ems.entity.*;
import com.ems.util.CBBasePage;
import com.ems.util.MyProcess;
import com.ems.util.Pagination;

/**
 *@文件名称：MxcbArticleDaoImp.java
 *@路径：com.ems.dao.impl
 *@作者：霍雨翔
 *@时间：2017-8-22
 *@版本：V1.0
 *@描述：TODO
 */
@Repository
public class MxcbArticleDaoImpl extends CBBasePage implements MxcbArticleDao{
	
	/**
	 * 浏览稿件
	 * @author 霍雨翔
     * @param pageNo
	 * @param pageSize
	 * @param articleLxName
	 * @param title
	 * @param author
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Pagination browse(int pageNo, int pageSize, String articleLxName, String title, String author, String beginTime, String endTime){
		StringBuilder queryString=new StringBuilder("from MxcbArticle ma where 1=1");
		if(MyProcess.notNone(title)){
			//queryString.append(" and ma.title like '%").append(title).append("%'");
			
			String[] keywordArray =title.split("[,\\，| |　]");         //间隔符可以是半角空格，全角空格，中文逗号，英文逗号。
        	
            if(keywordArray.length>1){                   //分两种情况，可以针对单词和多词进行查询，多词查询时用and连接，后面的or用小括号包住。
            	 queryString.append(" and (ma.title like '%").append(keywordArray[0]).append("%'");
            	 if(keywordArray.length==2){
            		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%')");
            	 }
            	 else{
            		 for(int i =1;i<keywordArray.length-1;i++){      //这里从下标是1开始循环。
                         queryString.append(" or ma.title like '%").append(keywordArray[i]).append("%'");
                     }
            		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%')");
            	 }
            }else{
            	queryString.append(" and ma.title like '%").append(title).append("%'");
            }
		}
		if(MyProcess.notNone(author)){
			queryString.append(" and ma.author like '%").append(author).append("%'");
		}
		if(MyProcess.notNone(beginTime)){
			queryString.append(" and ma.checkDate>=to_date('").append(beginTime).append(" 0:0:0','yyyy-MM-dd hh24:mi:ss')");
		}
		if(MyProcess.notNone(endTime)){
			queryString.append(" and ma.checkDate<=to_date('").append(endTime).append(" 23:59:59','yyyy-MM-dd hh24:mi:ss')");
		}
		if(MyProcess.notNone(articleLxName)&&!"不限".equals(articleLxName)){
			queryString.append(" and ma.lx='").append(articleLxName).append("'");
		}
		String countQueryString=new StringBuilder("select count(*) ").append(queryString).toString();
		String listQueryString=queryString.append(" order by ma.id desc").toString();
		return getCBPage(pageNo, pageSize, countQueryString, listQueryString);
	}
	
	
	public Pagination browseAll(int pageNo, int pageSize, String articleLxName, String title, String author, String beginTime, String endTime,Integer kplace){
		StringBuilder queryString=new StringBuilder("from MxcbArticle ma and MxcbPicture mx  where 1=1");
		
		//以下为switch临时。
        switch (kplace) {
        case 1:
        	if (MyProcess.notNone(title)) {
                //queryString.append(" and ir.issueTitle like '%").append(title).append("%'");

            	String[] keywordArray =title.split("[,\\，| |　]");         //间隔符可以是半角空格，全角空格，中文逗号，英文逗号。
            	
                if(keywordArray.length>1){                   //分两种情况，可以针对单词和多词进行查询，多词查询时用and连接，后面的or用小括号包住。
                	 queryString.append(" and (ma.title like '%").append(keywordArray[0]).append("%'");
                	 if(keywordArray.length==2){
                		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                	 }
                	 else{
                		 for(int i =1;i<keywordArray.length-1;i++){      //这里从下标是1开始循环。
                             queryString.append(" or ma.title like '%").append(keywordArray[i]).append("%'");
                         }
                		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                	 }
                }else{
                	queryString.append(" and ma.title like '%").append(title).append("%'");
                }
                break;
            }
        case 2:
        	if (MyProcess.notNone(title)) {
                //queryString.append(" and ir.issueTitle like '%").append(title).append("%'");

            	String[] keywordArray =title.split("[,\\，| |　]");         //间隔符可以是半角空格，全角空格，中文逗号，英文逗号。
            	
                if(keywordArray.length>1){                   //分两种情况，可以针对单词和多词进行查询，多词查询时用and连接，后面的or用小括号包住。
                	if(!articleLxName.equals("编译")&&!articleLxName.equals("文图")){
                		queryString.append(" and (mx.illustrate like '%").append(keywordArray[0]).append("%'");
                		
                		if(keywordArray.length==2){
                   		 	queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                   	 	}
                		else{
                   		 	for(int i =1;i<keywordArray.length-1;i++){      //这里从下标是1开始循环。
                                queryString.append(" or mx.illustrate like '%").append(keywordArray[i]).append("%'");
                            }
                   		 	queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                   	  	}
                	}else if(!articleLxName.equals("编译")&&articleLxName.equals("文图")){
                		queryString.append(" and (mx.illustrate like '%").append(keywordArray[0]).append("%'");
                		queryString.append(" or ma.content like '%").append(keywordArray[0]).append("%'");
                		
                		if(keywordArray.length==2){
                   		 	queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%'");
                   		    queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                   	 	}
                		else{
                   		 	for(int i =1;i<keywordArray.length-1;i++){      //这里从下标是1开始循环。
                                queryString.append(" or mx.illustrate like '%").append(keywordArray[i]).append("%'");
                                queryString.append(" or ma.content like '%").append(keywordArray[i]).append("%'");
                            }
                   		 	queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%'");
                   		    queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                   	  	}
                	}
                	else{
                		 queryString.append(" and (ma.content like '%").append(keywordArray[0]).append("%'");
                		 
                		 if(keywordArray.length==2){
                    		 queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                    	 }
                		 else{
                    		 for(int i =1;i<keywordArray.length-1;i++){      //这里从下标是1开始循环。
                                 queryString.append(" or ma.content like '%").append(keywordArray[i]).append("%'");
                             }
                    		 queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                    	 }
                	}
                	
                	 
                	    
                }else{
                	if(!articleLxName.equals("编译")&&!articleLxName.equals("文图")){
                		queryString.append(" and mx.illustrate like '%").append(title).append("%'");
                	}else if(!articleLxName.equals("编译")&&articleLxName.equals("文图")){
                		queryString.append(" and (mx.illustrate like '%").append(title).append("%'");
                		queryString.append(" or ma.content like '%").append(title).append("%')");
                	}
                	else{
                		queryString.append(" and ma.content like '%").append(title).append("%'");
                	}
                	
                }
                break;
            }
        default:
        	if (MyProcess.notNone(title)) {
                //queryString.append(" and ir.issueTitle like '%").append(title).append("%'");

            	String[] keywordArray =title.split("[,\\，| |　]");         //间隔符可以是半角空格，全角空格，中文逗号，英文逗号。
            	
                if(keywordArray.length>1){                   //分两种情况，可以针对单词和多词进行查询，多词查询时用and连接，后面的or用小括号包住。
                	 queryString.append(" and (ma.title like '%").append(keywordArray[0]).append("%'");
                	 if(!articleLxName.equals("编译")&&!articleLxName.equals("文图")){
                		 queryString.append(" or mx.illustrate like '%").append(keywordArray[0]).append("%'");
                	 }else if(!articleLxName.equals("编译")&&articleLxName.equals("文图")){
                		 queryString.append(" or mx.illustrate like '%").append(keywordArray[0]).append("%'");
                		 queryString.append(" or ma.content like '%").append(keywordArray[0]).append("%'");
                	 }
                	 else{
                		 queryString.append(" or ma.content like '%").append(keywordArray[0]).append("%'");
                	 }
                	 
                	 
                	 if(keywordArray.length==2){
                		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%'");
                		 if(!articleLxName.equals("编译")&&!articleLxName.equals("文图")){
                			 queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                		 }else if(!articleLxName.equals("编译")&&articleLxName.equals("文图")){
                			 queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%'");
                			 queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                		 }
                		 else{
                			 queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                		 }
                		 
                	 }
                	 else{
                		 for(int i =1;i<keywordArray.length-1;i++){      //这里从下标是1开始循环。
                             queryString.append(" or ma.title like '%").append(keywordArray[i]).append("%'");
                             if(!articleLxName.equals("编译")&&!articleLxName.equals("文图")){
                            	 queryString.append(" or mx.illustrate like '%").append(keywordArray[i]).append("%'");
                             }else if(!articleLxName.equals("编译")&&articleLxName.equals("文图")){
                            	 queryString.append(" or mx.illustrate like '%").append(keywordArray[i]).append("%'");
                            	 queryString.append(" or ma.content like '%").append(keywordArray[i]).append("%'");
                             }
                             else{
                            	 queryString.append(" or ma.content like '%").append(keywordArray[i]).append("%'");
                             }
                             
                         }
                		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%'");
                		 if(!articleLxName.equals("编译")&&!articleLxName.equals("文图")){
                			 queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                		 }else if(!articleLxName.equals("编译")&&articleLxName.equals("文图")){
                			 queryString.append(" or mx.illustrate like '%").append(keywordArray[keywordArray.length-1]).append("%'");
                			 queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                		 }
                		 else{
                			 queryString.append(" or ma.content like '%").append(keywordArray[keywordArray.length-1]).append("%')");
                		 }
                		 
                	 }
                }else{
                	queryString.append(" and (ma.title like '%").append(title).append("%'");
                	if(!articleLxName.equals("编译")&&!articleLxName.equals("文图")){
                		queryString.append(" or mx.illustrate like '%").append(title).append("%')");
                	}else if(!articleLxName.equals("编译")&&articleLxName.equals("文图")){
                		queryString.append(" or mx.illustrate like '%").append(title).append("%'");
                		queryString.append(" or ma.content like '%").append(title).append("%')");
                	}
                	else{
                		queryString.append(" or ma.content like '%").append(title).append("%')");
                	}
                	
                }
                break;
            }
        }
        //以上为switch临时。
		/*if(MyProcess.notNone(title)){
			//queryString.append(" and ma.title like '%").append(title).append("%'");
			
			String[] keywordArray =title.split("[,\\，| |　]");         //间隔符可以是半角空格，全角空格，中文逗号，英文逗号。
        	
            if(keywordArray.length>1){                   //分两种情况，可以针对单词和多词进行查询，多词查询时用and连接，后面的or用小括号包住。
            	 queryString.append(" and (ma.title like '%").append(keywordArray[0]).append("%'");
            	 if(keywordArray.length==2){
            		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%')");
            	 }
            	 else{
            		 for(int i =1;i<keywordArray.length-1;i++){      //这里从下标是1开始循环。
                         queryString.append(" or ma.title like '%").append(keywordArray[i]).append("%'");
                     }
            		 queryString.append(" or ma.title like '%").append(keywordArray[keywordArray.length-1]).append("%')");
            	 }
            }else{
            	queryString.append(" and ma.title like '%").append(title).append("%'");
            }
		}*/
		if(MyProcess.notNone(author)){
			queryString.append(" and ma.author like '%").append(author).append("%'");
		}
		if(MyProcess.notNone(beginTime)){
			queryString.append(" and ma.checkDate>=to_date('").append(beginTime).append(" 0:0:0','yyyy-MM-dd hh24:mi:ss')");
		}
		if(MyProcess.notNone(endTime)){
			queryString.append(" and ma.checkDate<=to_date('").append(endTime).append(" 23:59:59','yyyy-MM-dd hh24:mi:ss')");
		}
		if(MyProcess.notNone(articleLxName)&&!"不限".equals(articleLxName)){
			queryString.append(" and ma.lx='").append(articleLxName).append("'");
		}
		String countQueryString=new StringBuilder("select count(*) ").append(queryString).toString();
		String listQueryString=queryString.append(" order by ma.id desc").toString();
		
		//System.out.println(listQueryString);
		return getCBPage(pageNo, pageSize, countQueryString, listQueryString);
	}
		
	/**
	 * 查询
	 * @author 霍雨翔
	 * @param id
	 * @return MxcbArticle
	 */
	@Override
	public MxcbArticle findById(Long id) {
		List<MxcbArticle> list=getHibernateTemplate().find("from MxcbArticle ma where ma.id=?", id);
		if(list.size()>0)
			return list.get(0);
		else
			return null;
	}

	/**
	 * 保存
	 * @author 霍雨翔
	 * @param mxcbArticle
	 */
	@Override
	public void save(MxcbArticle mxcbArticle) {
		getHibernateTemplate().save(mxcbArticle);
	}
	
}