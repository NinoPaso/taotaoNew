package service;

import com.taotao.common.utils.TaotaoResult;

public interface RedisService {

	TaotaoResult syncContent(long contentCid);
}
