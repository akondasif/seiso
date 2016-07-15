package com.expedia.seiso.web.controller;

import com.expedia.seiso.domain.entity.Node;
import com.expedia.seiso.domain.repo.NodeRepo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;

/**
 * Created by imccunn on 7/5/16.
 *
 * These endpoints are used specifically for the service-instance dashboard page in seiso-ui. They construct
 * an appropriate payload for any component that displays a list of nodes in one http request.
 *
 */
@RestController
@BasePathAwareController
@RequestMapping(value = "/internal/serviceInstance")
@Slf4j
public class NodeController {

    @Autowired
    private NodeRepo nodeRepo;

    @RequestMapping(value = "/{key}/nodes", method = RequestMethod.GET)
    @ResponseBody
    public Page<Node> getServiceInstanceNodes(@PathVariable("key") String key, Pageable pageable) {
        Page<Node> siNodePage = nodeRepo.findByServiceInstanceKey(key, pageable);
        return stripNodeCircularProperties(siNodePage);
    }

    @RequestMapping(value = "/{key}/nodeAlerts", method = RequestMethod.GET)
    @ResponseBody
    public Page<Node> getServiceInstanceNodeAlerts(@PathVariable("key") String key, Pageable pageable) {
        Page<Node> siNodePage = nodeRepo.findNodeAlertsByServiceInstance(key, pageable);
        return stripNodeCircularProperties(siNodePage);
    }

    // Spring, with jackson, will attempt to serialize a payload that has circular references
    // so we need to null those out here. [IDM]
    private Page<Node> stripNodeCircularProperties(Page<Node> nodePage) {
        val pageContent = nodePage.getContent();
        for (val aNode : pageContent) {
            aNode.setServiceInstance(null);
            aNode.setMachine(null);
            for(val aIp : aNode.getIpAddresses()) {
                aIp.setNode(null);
                aIp.getIpAddressRole().setServiceInstance(null);
                aIp.getIpAddressRole().setIpAddresses(null);
                for (val ep : aIp.getEndpoints()) {
                    ep.setIpAddress(null);
                    ep.getPort().setServiceInstance(null);
                    ep.getPort().setEndpoints(null);
                }
            }
        }
        return nodePage;
    }
}
