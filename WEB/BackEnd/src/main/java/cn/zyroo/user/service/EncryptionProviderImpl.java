package cn.zyroo.user.service;

import cn.zyroo.common.service.EncryptionProvider;
import cn.zyroo.user.utils.EncryptionUtil;
import org.springframework.stereotype.Service;

@Service
public class EncryptionProviderImpl implements EncryptionProvider {

    @Override
    public String encrypt(String data) {
        return EncryptionUtil.encrypt(data);
    }

    @Override
    public String decrypt(String encryptedData) {
        return EncryptionUtil.decrypt(encryptedData);
    }
}