package com.zcbspay.platform.instead.batch.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcbspay.platform.business.order.bean.BatchResultBean;
import com.zcbspay.platform.business.order.bean.FileContentBean;
import com.zcbspay.platform.business.order.bean.ResultBean;
import com.zcbspay.platform.instead.batch.bean.BatchQueryReqBean;
import com.zcbspay.platform.instead.batch.bean.BatchQueryResBean;
import com.zcbspay.platform.instead.batch.service.CollectAndPayService;
import com.zcbspay.platform.instead.common.bean.MessageBean;
import com.zcbspay.platform.instead.common.bean.UrlBean;
import com.zcbspay.platform.instead.common.constant.Constants;
import com.zcbspay.platform.instead.common.enums.ResponseTypeEnum;
import com.zcbspay.platform.instead.common.exception.DataErrorException;
import com.zcbspay.platform.instead.common.utils.BeanCopyUtil;
import com.zcbspay.platform.instead.common.utils.ExceptionUtil;
import com.zcbspay.platform.instead.common.utils.FlaterUtils;
import com.zcbspay.platform.instead.common.utils.HttpRequestParam;
import com.zcbspay.platform.instead.common.utils.HttpUtils;
import com.zcbspay.platform.instead.common.utils.ValidateLocator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 批量查询服务
 * @author: zhangshd
 * @date:   2017年5月3日 上午9:48:19   
 * @version :v1.0
 */
@Service("batchQueryService")
public class BatchQueryServiceImpl implements CollectAndPayService {
	private static final Logger logger = LoggerFactory.getLogger(BatchQueryServiceImpl.class);
//	@Autowired
//	private OrderQueryService batchTradeQuery;
	@Autowired
	private UrlBean urlBean;

	@Override
	public MessageBean invoke(MessageBean messageBean) {
		BatchQueryResBean batchQueryResBean=new BatchQueryResBean();
		try {
			BatchQueryReqBean reqBean = (BatchQueryReqBean) JSONObject.toBean(JSONObject.fromObject(messageBean.getData()),
					BatchQueryReqBean.class);
			batchQueryResBean = BeanCopyUtil.copyBean(BatchQueryResBean.class, reqBean);
			ValidateLocator.validateBeans(reqBean);
			ResultBean resultBean = null;
			String url = null;
			if ("01".equals(reqBean.getBusiType())) {// 批量代收
				url=urlBean.getBatchQueryCollectUrl();
			} else if ("02".equals(reqBean.getBusiType())) {// 批量代付
				url=urlBean.getBatchQueryPayUrl();
			}
			
			HttpRequestParam httpRequestParam= new HttpRequestParam("merid",reqBean.getMerId());
			HttpRequestParam httpRequestParam1= new HttpRequestParam("batchno",reqBean.getBatchNo());
			HttpRequestParam httpRequestParam2= new HttpRequestParam("txndate",reqBean.getTxnDate());
			List<HttpRequestParam> list = new ArrayList<>();
			list.add(httpRequestParam);
			list.add(httpRequestParam1);
			list.add(httpRequestParam2);
			
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.openConnection();
			String responseContent = httpUtils.executeHttpPost(url,list,Constants.Encoding.UTF_8);
			httpUtils.closeConnection();
			resultBean=(ResultBean) JSONObject.toBean(JSONObject.fromObject(responseContent), ResultBean.class);
			//resultBean = batchTradeQuery.queryConcentrateCollectionBatch(reqBean.getMerId(), reqBean.getBatchNo(),reqBean.getTxnDate());
			
			if (!resultBean.isResultBool()) {
				ResponseTypeEnum responseTypeEnum=ResponseTypeEnum.getTxnTypeEnumByInCode(resultBean.getErrCode());
				if (responseTypeEnum!=null) {
					batchQueryResBean.setRespCode(responseTypeEnum.getCode());
					batchQueryResBean.setRespMsg(resultBean.getErrMsg().toString());
				}else {
					batchQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
					batchQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
				}
				
			} else {
				batchQueryResBean.setRespCode(ResponseTypeEnum.success.getCode());
				batchQueryResBean.setRespMsg(ResponseTypeEnum.success.getMessage());
				
				Map<String, Class> mapClass=new HashMap<>();
				mapClass.put("resultObj", FileContentBean.class);
				resultBean=(ResultBean) JSONObject.toBean(JSONObject.fromObject(responseContent), ResultBean.class,mapClass);
				BatchResultBean batchResultBean = (BatchResultBean) resultBean.getResultObj();
				BeanCopyUtil.copyBean(batchQueryResBean, batchResultBean);
				batchQueryResBean.setFileContent(
						FlaterUtils.deflater(JSONArray.fromObject(batchResultBean.getFileContentList()).toString()));
			}
		} catch (DataErrorException e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchQueryResBean.setRespCode(ResponseTypeEnum.dataError.getCode());
			batchQueryResBean.setRespMsg(ResponseTypeEnum.dataError.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtil.getStackTrace(e));
			batchQueryResBean.setRespCode(ResponseTypeEnum.fail.getCode());
			batchQueryResBean.setRespMsg(ResponseTypeEnum.fail.getMessage());
		}
		messageBean.setData(JSONObject.fromObject(batchQueryResBean).toString());
		return messageBean;
	}

}
