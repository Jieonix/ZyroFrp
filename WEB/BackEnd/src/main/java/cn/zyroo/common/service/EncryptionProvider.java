package cn.zyroo.common.service;

/**
 * 加密提供者接口
 * 定义通用的加密解密方法
 * 业务模块需要实现此接口
 */
public interface EncryptionProvider {

    /**
     * 加密数据
     *
     * @param data 原始数据
     * @return 加密后的数据
     */
    String encrypt(String data);

    /**
     * 解密数据
     *
     * @param encryptedData 加密后的数据
     * @return 解密后的数据
     */
    String decrypt(String encryptedData);
}