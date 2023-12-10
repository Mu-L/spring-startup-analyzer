package io.github.linyimin0812.async.config

import spock.lang.Specification

/**
 * @author linyimin
 * */
class AsyncConfigSpec extends Specification {

    def "test getInstance"() {
        when:
        AsyncConfig instance = AsyncConfig.getInstance()

        then:
        AsyncConfig.getInstance()
    }

    def "test getAsyncBeanProperties"() {
        when:
        AsyncBeanProperties asyncBeanProperties = AsyncBeanPropertiesSpec.parsePropertiesFromFile()
        AsyncConfig.getInstance().setAsyncBeanProperties(asyncBeanProperties)

        then:
        AsyncConfig.getInstance().getAsyncBeanProperties() == asyncBeanProperties
    }

   def "test setAsyncBeanProperties"() {
       when:
       AsyncBeanProperties asyncBeanProperties = AsyncBeanPropertiesSpec.parsePropertiesFromFile()
       AsyncConfig.getInstance().setAsyncBeanProperties(asyncBeanProperties)

       then:
       AsyncConfig.getInstance().getAsyncBeanProperties() == asyncBeanProperties
   }

   def "test isAsyncBean"() {
       when:
       AsyncConfig.getInstance().setAsyncBeanProperties(AsyncBeanPropertiesSpec.parsePropertiesFromFile())

       then:
       AsyncConfig.getInstance().isAsyncBean("testBean")
   }
}