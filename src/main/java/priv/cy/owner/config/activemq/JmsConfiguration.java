package priv.cy.owner.config.activemq;

/**
 * @author ：cuiyang
 * @description：同时支持queue和topic
 * @date ：Created in 2020/5/24 12:46
 */
//@Configuration
//@EnableJms
//public class JmsConfiguration {
//    /**
//     * topic模式的ListenerContainer
//     *
//     * @param activeMQConnectionFactory
//     * @return
//     */
//    @Bean
//    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
//        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
//        bean.setPubSubDomain(true);
//        bean.setConnectionFactory(activeMQConnectionFactory);
//        /**
//         * 使用消息转换器
//         */
//        bean.setMessageConverter(jacksonJmsMessageConverter());
//        return bean;
//    }
//
//    /**
//     * queue模式的ListenerContainer
//     *
//     * @param activeMQConnectionFactory
//     * @return
//     */
//    @Bean
//    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory activeMQConnectionFactory) {
//        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
//        bean.setConnectionFactory(activeMQConnectionFactory);
//        /**
//         * 使用消息转换器
//         */
//        bean.setMessageConverter(jacksonJmsMessageConverter());
//        return bean;
//    }
//
//    /**
//     * 消息转换器
//     *
//     * @return
//     */
//    @Bean
//    public MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        converter.setTypeIdPropertyName("_type");
//        return converter;
//    }
//}
