package com.manageyourtools.toolroom.catalogue.domain

import com.manageyourtools.toolroom.catalogue.dto.CategoryDto
import com.manageyourtools.toolroom.catalogue.dto.ToolAllQuantityChange
import com.manageyourtools.toolroom.catalogue.dto.ToolCurrentQuantityChange
import com.manageyourtools.toolroom.catalogue.dto.ToolQueryDto
import com.manageyourtools.toolroom.exception.ResourceNotFoundException
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.ConcurrentHashMap

class CatalogueFacadeTest extends Specification implements SampleTools, SampleCategories {

    @Shared
    Map<Long, Tool> toolDb = new ConcurrentHashMap<>()
    @Shared
    Map<Long, ToolImage> toolImageDb = new ConcurrentHashMap<>()
    @Shared
    Map<Long, Category> categoryDb = new ConcurrentHashMap<>()
    @Shared
    CatalogueFacade facade = new CatalogueConfiguration().catalogueFacade(toolDb, toolImageDb, categoryDb)

    def cleanup() {
        toolDb.clear()
        toolImageDb.clear()
        categoryDb.clear()
    }

    def "should show tool"() {
        given: "tool_id is in the db"
            toolDb.put(hammer.toDto().id, hammer)

        when:
            ToolQueryDto toolQueryDto = facade.findToolById(hammer.toDto().id)

        then: "tool_id is retrieved"
            toolQueryDto == hammer.toQuery()
    }

    def "should throw exception if there is no tool"() {
        when:
        facade.findToolById(hammer.toDto().id)

        then:
        thrown(ResourceNotFoundException)
    }

    def "should show all tools"() {
        given: "there is 2 tools in the system"
            toolDb.put(hammer.toDto().id, hammer)
            toolDb.put(drill.toDto().id, drill)

        expect: "all tools are retrieved"
            facade.findAllTools().size() == 2
    }

    def "should show tools which their all count is below minimal count value "() {
        given: "there is 1 tool_id with tool_id all count below minimal count"
            toolDb.put(hammer.toDto().id, hammer)
            toolDb.put(drill.toDto().id, drill)

        when: "there is request for tool_id with shortages"
            Set<ToolQueryDto> tools = facade.findToolsWithCountSmallerThanMinimal()

        then: "one tool_id is retrieved"
            tools.size() == 1
            tools.contains(hammer.toQuery())
    }

    def "should show tools for which warranty date is before date from the next week"() {
        given: "in system there is one tool_id which warranty date will be this week"
            toolDb.put(hammer.toDto().id, hammer)
            toolDb.put(drill.toDto().id, drill)

        when: "system is asked for data"
            Set<ToolQueryDto> tools = facade.findToolsWithCloseWarrantyTime(Interval.WEEK)

        then: "one tool_id is returned"
            tools.size() == 1
            tools.contains(drill.toQuery())
    }

    def "should save tool"() {
        when: "there is try to save tool_id"
            facade.saveTool(hammer.toDto())
        then: "tool_id is saved"
            toolDb.size() == 1
            toolDb.get(hammer.toDto().id) == hammer
    }

    def "should disable tool which count is zero"() {
        given: "there is tool_id in the system with no all_count"
            toolDb.put(hammer.toDto().id, hammer)

        when: "system is asked to disable the tool_id"
            facade.disableTool(hammer.toDto().id)

        then: "tool_id is disabled"
            !toolDb.get(hammer.toDto().id).toQuery().isEnable
    }

    def "should throw exception if there is try to disable tool with non zero count"() {
        given: "there is tool_id in the system with non zero all_count"
            toolDb.put(hammer.toDto().id, hammer)

        when: "system is asked to disable the tool_id"
            facade.disableTool(hammer.toDto().id)

        then: "tool_id is disabled"
            thrown(IllegalArgumentException)
    }

    def "should add tool all quantity"() {
        given: "there is tool_id in the system"
        toolDb.put(hammer.toDto().id, hammer)
        long quantityChange = 2
        ToolAllQuantityChange toolAllQuantityChange = new ToolAllQuantityChange(hammer.toDto().id, quantityChange)

        when:
        facade.calculateToolAllQuantity(toolAllQuantityChange)

        then: "tool_id quantity is increased"
        ToolQueryDto after = toolDb.get(hammer.toDto().id).toQuery()
        after.allCount == quantityChange + hammer.toQuery().allCount
        after.currentCount == quantityChange + hammer.toQuery().currentCount
    }

    def "should remove tool all quantity"() {
        given: "there is tool_id in the system with enough quantity"
        toolDb.put(hammer.toDto().id, hammer)
        long quantityChange = -2
        ToolAllQuantityChange toolAllQuantityChange = new ToolAllQuantityChange(hammer.toDto().id, quantityChange)

        when:
        facade.calculateToolAllQuantity(toolAllQuantityChange)

        then: "tool_id quantity is decreased"
        ToolQueryDto after = toolDb.get(hammer.toDto().id).toQuery()
        after.allCount == quantityChange + hammer.toQuery().allCount
        after.currentCount == quantityChange + hammer.toQuery().currentCount
    }

    def "should throw exception if there is no enough tool all quantity"() {
        given: "there is tool_id in the system with not enough quantity"
        toolDb.put(hammer.toDto().id, hammer)
        long quantityChange = -4
        ToolAllQuantityChange toolAllQuantityChange = new ToolAllQuantityChange(hammer.toDto().id, quantityChange)

        when:
        facade.calculateToolAllQuantity(toolAllQuantityChange)

        then: "exception is thrown"
        thrown(IllegalStateException)
    }

    def "should throw exception during calculation of all quantity if tool is disabled"() {
        given: "there is tool_id in the system which is disabled"
        toolDb.put(drill.toDto().id, drill)
        facade.disableTool(drill.toDto().id)
        long quantityChange = 1
        ToolAllQuantityChange toolAllQuantityChange = new ToolAllQuantityChange(drill.toDto().id, quantityChange)

        when:
        facade.calculateToolAllQuantity(toolAllQuantityChange)

        then: "exception is thrown"
        thrown(IllegalStateException)
    }

    def "should pick up tool from warehouse"() {
        given: "there is tool_id in the system with enough count"
        toolDb.put(hammer.toDto().id, hammer)
        long quantityChange = -2
        ToolCurrentQuantityChange toolCurrentQuantityChange = new ToolCurrentQuantityChange(hammer.toDto().id, quantityChange)

        when:
        facade.calculateToolCurrentQuantity(toolCurrentQuantityChange)

        then: "tool_id current quantity is decreased"
        ToolQueryDto after = toolDb.get(hammer.toDto().id).toQuery()
        after.currentCount == quantityChange + hammer.toQuery().currentCount
    }

    def "should return tool to warehouse"() {
        given: "there is tool_id in the system which was picked up"
        toolDb.put(hammer.toDto().id, hammer)
        long quantityChange = 2
        facade.calculateToolCurrentQuantity(new ToolCurrentQuantityChange(hammer.toDto().id, -quantityChange))

        when:
        facade.calculateToolCurrentQuantity(new ToolCurrentQuantityChange(hammer.toDto().id, quantityChange))

        then: "tool_id quantity is decreased"
        ToolQueryDto after = toolDb.get(hammer.toDto().id).toQuery()
        after.allCount == after.currentCount
        after.currentCount == hammer.toQuery().currentCount
    }

    def "should throw exception if there is no enough tool to pick up from warehouse"() {
        given: "there is tool_id in the system"
        toolDb.put(hammer.toDto().id, hammer)
        long quantityChange = -4
        ToolCurrentQuantityChange toolCurrentQuantityChange = new ToolCurrentQuantityChange(hammer.toDto().id, quantityChange)

        when:
        facade.calculateToolCurrentQuantity(toolCurrentQuantityChange)

        then: "exception is thrown"
        thrown(IllegalStateException)
    }

    def "should throw exception when there is try to pick up disabled tool"() {
        given: "there is tool_id in the system"
        toolDb.put(drill.toDto().id, drill)
        facade.disableTool(drill.toDto().id)
        long quantityChange = 1
        ToolCurrentQuantityChange toolCurrentQuantityChange = new ToolCurrentQuantityChange(hammer.toDto().id, quantityChange)

        when:
        facade.calculateToolCurrentQuantity(toolCurrentQuantityChange)

        then: "exception is thrown"
        thrown(IllegalStateException)
    }

    def "should throw exception if there is no image for tool"() {
        given: "there is tool_id in the system without image"
            toolDb.put(hammer.toDto().id, hammer)

        when: "system is asked for tool_id image"
            facade.findImage(hammer.toDto().id)
        then:
            thrown(ResourceNotFoundException)
    }

    def "should upload tool image"() {
        given: "there is tool_id in the system"
            toolDb.put(hammer.toDto().id, hammer)
            byte[] testBytes = "TEST".bytes
            MockMultipartFile multipartFile = new MockMultipartFile("test", testBytes)

        when: "there is try to upload image for the tool_id"
            facade.uploadImage(multipartFile, hammer.toDto().id)

        then:
            facade.findImage(hammer.toDto().id) == testBytes
    }

    def "should show tool image"() {
        given:
            byte[] data = "TEST".bytes
            ToolImage toolImage = ToolImage.builder().id(1).toolId(hammer.toDto().id).image(data).build()
            toolImageDb.put(hammer.toDto().id, toolImage)

        when:
            byte[] image = facade.findImage(hammer.toDto().id)

        then:
            image == data
    }

    def "should show category"() {
        given:
            categoryDb.put(electric.toDto().id, electric)

        when:
            CategoryDto categoryDto = facade.findCategory(electric.toDto().id)

        then:
            categoryDto.getName() == electric.toDto().name
    }

    def "should show all categories"() {
        given:
            categoryDb.put(electric.toDto().id, electric)
            categoryDb.put(mechanical.toDto().id, mechanical)

        when:
            Set<CategoryDto> categories = facade.findAllCategories()

        then:
            categories.size() == 2
    }

    def "should add new category"() {
        when:
            facade.saveCategory(electric.toDto())
        then:
            categoryDb.size() == 1
            categoryDb.get(electric.toDto().id).toDto().name == electric.toDto().name
    }

    def "should delete category"() {
        given:
            categoryDb.put(electric.toDto().id, electric)

        when:
            facade.deleteCategory(electric.toDto().id)

        then:
            categoryDb.size() == 0
    }
}
