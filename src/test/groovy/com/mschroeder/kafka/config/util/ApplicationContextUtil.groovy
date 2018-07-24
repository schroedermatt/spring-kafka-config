package com.mschroeder.kafka.config.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.GenericApplicationContext
import org.springframework.stereotype.Component

@Component
class ApplicationContextUtil {
    @Autowired
    GenericApplicationContext context

    /**
     * Wipes out the existing bean (if in use) and registers
     * a new bean instance under the same name.
     * @param beanName name of bean to update
     * @param clazz bean Class
     * @param updatedBean updated bean object
     */
    void updateBean(String beanName, Class clazz, def updatedBean) {
        if (context.isBeanNameInUse(beanName)) {
            context.removeBeanDefinition(beanName)
        }

        context.registerBean(beanName, clazz, { updatedBean })
    }
}
