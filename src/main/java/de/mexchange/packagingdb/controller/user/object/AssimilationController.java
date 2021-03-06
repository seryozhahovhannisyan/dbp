package de.mexchange.packagingdb.controller.user.object;

import de.mexchange.packagingdb.common.exception.DataNotFoundException;
import de.mexchange.packagingdb.common.exception.InternalErrorException;
import de.mexchange.packagingdb.controller.response.AjaxResponse;
import de.mexchange.packagingdb.domain.AssimilationList;
import de.mexchange.packagingdb.service.AssimilationListService;
import de.mexchange.packagingdb.service.LanguageService;
import de.mexchange.packagingdb.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by Arthur on 5/23/2016.
 */
@Controller
@RequestMapping("/assimilationlist")
public class AssimilationController extends BaseObjectController<AssimilationList> {

    private Validator validator;

    @Autowired
    public AssimilationController(AssimilationListService assimilationListService , MessageService messageService, LanguageService languageService) {
        super (assimilationListService, messageService, languageService, AssimilationList.class);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (validator == null) {
            validator = binder.getValidator();
        }
    }

    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(AssimilationList assimilationList, Model model) {
        model.addAttribute("assimilationList", assimilationList);
        return super.addView(assimilationList);
    }

    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid final AssimilationList assimilationList,
                      BindingResult result,
                      @RequestParam(value = "file", required = false) MultipartFile dataSource,
                      final RedirectAttributes redirectAttributes) throws InternalErrorException {

        validator.validate(assimilationList, result);
        if (!result.hasErrors() && !dataSource.isEmpty()) {
            try {
                assimilationList.setDataSource(dataSource.getBytes());
                assimilationList.setDataSourceContentType(dataSource.getContentType());
                assimilationList.setDataSourceFilename(dataSource.getOriginalFilename());
            } catch (IOException e) {
                result.addError(new FieldError("dataSource", "dataSourceFilename", "Failed to attach dataSource file"));
            }
        }
        return super.add(assimilationList, result, redirectAttributes);
    }


    @RequestMapping(value = "/dataSource/{id}", method = RequestMethod.GET)
    public void downloadDataSource(@PathVariable("id") Long id,
                                 HttpServletResponse response) throws InternalErrorException, DataNotFoundException, IOException {
        AssimilationList assimilationList = modelService.getByID(id);
        response.setContentType(assimilationList.getDataSourceContentType());
        response.setHeader("Content-disposition", "attachment; filename=" + assimilationList.getDataSourceFilename());
        response.getOutputStream().write(assimilationList.getDataSource());
        response.flushBuffer();
    }

    @PreAuthorize("hasRole('ROLE_READER') or hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) throws InternalErrorException {
        return super.list(model);
    }

    @PreAuthorize("hasRole('ROLE_READER') or hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/list-items", method = RequestMethod.GET)
    public @ResponseBody
    AjaxResponse listItems(@RequestParam(value = "page", required = false) int page,
                           @RequestParam(value = "count", required = false, defaultValue = "10") int count) {
        return super.listItems(page, count);
    }

    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Long id, Model model) throws InternalErrorException, DataNotFoundException {
        return super.editView(id, model);
    }

    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@Valid final AssimilationList assimilationList,
                       @RequestParam(value = "dataSourceRemoved", required = false) boolean dataSourceRemoved,
                       @RequestParam(value = "file", required = false) MultipartFile dataSource,
                       BindingResult result,
                       final RedirectAttributes redirectAttributes) throws DataNotFoundException, InternalErrorException {

        validator.validate(assimilationList, result);
        if (dataSourceRemoved) {
            assimilationList.setDataSourceFilename(null);
        } else if (!result.hasErrors() && !dataSource.isEmpty()) {
            try {
                assimilationList.setDataSource(dataSource.getBytes());
                assimilationList.setDataSourceContentType(dataSource.getContentType());
                assimilationList.setDataSourceFilename(dataSource.getOriginalFilename());
            } catch (IOException e) {
                result.addError(new FieldError("assimilationList", "dataSourceFilename", "Failed to attach Data Source file"));
            }
        }
        return super.edit(assimilationList, result, redirectAttributes);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(@RequestParam(value = "id", required = false) List<Long> ids,
                         final RedirectAttributes redirectAttributes) throws DataNotFoundException, InternalErrorException {
        return super.remove(ids, redirectAttributes);
    }
}
