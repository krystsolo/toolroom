package com.manageyourtools.toolroom.catalogue.domain

import com.manageyourtools.toolroom.catalogue.dto.CategoryDto
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
        given: "tool is in the db"
            toolDb.put(hammer.toDto().id, hammer)

        when:
            ToolQueryDto toolQueryDto = facade.findToolById(hammer.toDto().id)

        then: "tool is retrieved"
            toolQueryDto == hammer.toQuery()
    }

    def "should show all tools"() {
        given: "there is 2 tools in the system"
            toolDb.put(hammer.toDto().id, hammer)
            toolDb.put(drill.toDto().id, drill)

        expect: "all tools are retrieved"
            facade.findAllTools().size() == 2
    }

    def "should show tools which their all count is below minimal count value "() {
        given: "there is 1 tool with tool all count below minimal count"
            toolDb.put(hammer.toDto().id, hammer)
            toolDb.put(drill.toDto().id, drill)

        when: "there is request for tool with shortages"
            Set<ToolQueryDto> tools = facade.findToolsWithCountSmallerThanMinimal()

        then: "one tool is retrieved"
            tools.size() == 1
            tools.contains(hammer.toQuery())
    }

    def "should show tools for which warranty date is before date from the next week"() {
        given: "in system there is one tool which warranty date will be this week"
            toolDb.put(hammer.toDto().id, hammer)
            toolDb.put(drill.toDto().id, drill)

        when: "system is asked for data"
            Set<ToolQueryDto> tools = facade.findToolsWithCloseWarrantyTime(Interval.WEEK)

        then: "one tool is returned"
            tools.size() == 1
            tools.contains(drill.toQuery())
    }

    def "should save tool"() {
        when: "there is try to save tool"
            facade.saveTool(hammer.toDto())
        then: "tool is saved"
            toolDb.size() == 1
            toolDb.get(hammer.toDto().id) == hammer
    }

    def "should disable tool which count is zero"() {
        given: "there is tool in the system with no all_count"
            toolDb.put(hammer.toDto().id, hammer)

        when: "system is asked to disable the tool"
            facade.disableTool(hammer.toDto().id)

        then: "tool is disabled"
            !toolDb.get(hammer.toDto().id).toQuery().isEnable
    }

    def "should throw exception if there is try to disable tool with non zero count"() {
        given: "there is tool in the system with non zero all_count"
            toolDb.put(hammer.toDto().id, hammer)

        when: "system is asked to disable the tool"
            facade.disableTool(hammer.toDto().id)

        then: "tool is disabled"
            thrown(IllegalArgumentException)
    }

    def "should throw exception if there is no image for tool"() {
        given: "there is tool in the system without image"
            toolDb.put(hammer.toDto().id, hammer)

        when: "system is asked for tool image"
            facade.findImage(hammer.toDto().id)
        then:
            thrown(ResourceNotFoundException)
    }

    def "should upload tool image"() {
        given: "there is tool in the system"
            toolDb.put(hammer.toDto().id, hammer)
            byte[] testBytes = "TEST".bytes
            MockMultipartFile multipartFile = new MockMultipartFile("test", testBytes)

        when: "there is try to upload image for the tool"
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
            categoryDto.name = electric.toDto().name
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
