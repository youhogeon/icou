package com.youhogeon.icou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youhogeon.icou.repository.ResourceRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public abstract class ResourceService {
    
    private ResourceRepository resourceRepository;

    private static final char[] CHARS = "abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

    @Autowired
    private void setResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    private String generateKey(int length) {
        char[] result = new char[length];

        for (int i = 0; i < length; i++) {
            result[i] = CHARS[(int) (Math.random() * CHARS.length)];
        }

        return new String(result);
    }

    public String generateKey() {
        int length = 4;

        while (true) {
            String key = generateKey(length++);

            if (!resourceRepository.existsByToken(key)) {
                return key;
            }
        }
    }

}
