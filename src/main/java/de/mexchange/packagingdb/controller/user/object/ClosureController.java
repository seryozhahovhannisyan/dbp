package de.mexchange.packagingdb.controller.user.object;

import de.mexchange.packagingdb.common.exception.DataNotFoundException;
import de.mexchange.packagingdb.common.exception.InternalErrorException;
import de.mexchange.packagingdb.controller.response.AjaxResponse;
import de.mexchange.packagingdb.domain.Closure;
import de.mexchange.packagingdb.domain.Gasket;
import de.mexchange.packagingdb.domain.lcp.Language;
import de.mexchange.packagingdb.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

/**
 * Created by Garik on 5/22/16.
 */
@Controller
@RequestMapping("/closure")
public class ClosureController extends BaseObjectController<Closure> {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private GasketService gasketService;

    @Autowired
    private CoatingService coatingService;

	@Autowired
	public ClosureController(ClosureService closureService, MessageService messageService, LanguageService languageService) {
		super(closureService, messageService, languageService, Closure.class);
	}

    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Closure closure, Model model) throws InternalErrorException {
        prepare(closure, model);
        return super.addView(closure);
    }

    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@Valid final Closure closure,
                      BindingResult result,
                      final RedirectAttributes redirectAttributes) throws InternalErrorException {

        return super.add(closure, result, redirectAttributes);
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
    public String edit(@PathVariable("id") Long id,
                       Model model) throws InternalErrorException, DataNotFoundException {

        prepare(model);
        return super.editView(id, model);
    }

    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@Valid final Closure closure,
                       BindingResult result,
                       final RedirectAttributes redirectAttributes) throws DataNotFoundException, InternalErrorException {

        return super.edit(closure, result, redirectAttributes);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String remove(@RequestParam(value = "id", required = false) List<Long> ids,
                       final RedirectAttributes redirectAttributes) throws DataNotFoundException, InternalErrorException {

        return super.remove(ids, redirectAttributes);
    }

    @Override
    protected void prepare(Closure closure, Model model) throws InternalErrorException {
        prepare(model);
    }

    @Override
    protected void prepare(Model model) throws InternalErrorException {
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("materials", materialService.getAll(Language.localeOf(locale)));
        model.addAttribute("gaskets", gasketService.getAll(Language.localeOf(locale)));
        model.addAttribute("coatings", coatingService.getAll(Language.localeOf(locale)));
    }
}
