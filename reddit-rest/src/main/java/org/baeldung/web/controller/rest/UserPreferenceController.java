package org.baeldung.web.controller.rest;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/user/preference")
class UserPreferenceController {

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private ModelMapper modelMapper;

    //

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public PreferenceDto getCurrentUserPreference() {
        final Preference pref = getCurrentUser().getPreference();
        return modelMapper.map(pref, PreferenceDto.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody final PreferenceDto prefDto) {
        final Preference pref = modelMapper.map(prefDto, Preference.class);
        pref.setEmail(preferenceReopsitory.findOne(pref.getId()).getEmail());
        preferenceReopsitory.save(pref);
        getCurrentUser().setPreference(pref);
    }

    //

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }
}
