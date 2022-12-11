-- Only works with freshly reseted database

INSERT INTO `arrowhead`.`system_` (`id`, `system_name`, `address`, `port`) VALUES ('9', 'heatingsystem', '127.0.0.1', '8662');
INSERT INTO `arrowhead`.`system_` (`id`, `system_name`, `address`, `port`) VALUES ('10', 'tray', '127.0.0.1', '8764');

INSERT INTO `arrowhead`.`authorization_intra_cloud` (`consumer_system_id`, `provider_system_id`, `service_id`) VALUES ('9', '5', '23');
INSERT INTO `arrowhead`.`authorization_intra_cloud` (`consumer_system_id`, `provider_system_id`, `service_id`) VALUES ('9', '5', '24');
INSERT INTO `arrowhead`.`authorization_intra_cloud` (`consumer_system_id`, `provider_system_id`, `service_id`) VALUES ('8', '6', '25');
INSERT INTO `arrowhead`.`authorization_intra_cloud` (`consumer_system_id`, `provider_system_id`, `service_id`) VALUES ('8', '7', '26');
INSERT INTO `arrowhead`.`authorization_intra_cloud` (`consumer_system_id`, `provider_system_id`, `service_id`) VALUES ('10', '8', '27');

INSERT INTO `arrowhead`.`authorization_intra_cloud_interface_connection` (`id`, `authorization_intra_cloud_id`, `interface_id`) VALUES ('1', '1', '1');
INSERT INTO `arrowhead`.`authorization_intra_cloud_interface_connection` (`id`, `authorization_intra_cloud_id`, `interface_id`) VALUES ('2', '2', '1');
INSERT INTO `arrowhead`.`authorization_intra_cloud_interface_connection` (`id`, `authorization_intra_cloud_id`, `interface_id`) VALUES ('3', '3', '1');
INSERT INTO `arrowhead`.`authorization_intra_cloud_interface_connection` (`id`, `authorization_intra_cloud_id`, `interface_id`) VALUES ('4', '4', '1');
INSERT INTO `arrowhead`.`authorization_intra_cloud_interface_connection` (`id`, `authorization_intra_cloud_id`, `interface_id`) VALUES ('5', '5', '1');