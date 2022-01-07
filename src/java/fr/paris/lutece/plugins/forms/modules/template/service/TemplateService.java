/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.forms.modules.template.service;

import java.util.List;

import fr.paris.lutece.plugins.forms.business.FormDisplay;
import fr.paris.lutece.plugins.forms.modules.template.business.TemplateDisplayHome;
import fr.paris.lutece.plugins.forms.modules.template.business.TemplateStepHome;
import fr.paris.lutece.plugins.forms.modules.template.web.CompositeTemplateGroupDisplay;
import fr.paris.lutece.plugins.forms.modules.template.web.CompositeTemplateQuestionDisplay;
import fr.paris.lutece.plugins.forms.modules.template.web.TemplateDisplayTree;
import fr.paris.lutece.plugins.forms.service.IFormDisplayService;
import fr.paris.lutece.plugins.forms.util.FormsConstants;
import fr.paris.lutece.plugins.forms.web.ICompositeDisplay;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class TemplateService implements ITemplateService
{
    private static final int DISPLAY_ROOT_PARENT_ID = 0;

    public static final String BEAN_NAME = "forms-template.templateService";

    @Override
    public List<ICompositeDisplay> getTemplateCompositeList( int nIdTemplate )
    {
        TemplateDisplayTree displayTree = new TemplateDisplayTree( nIdTemplate );
        return displayTree.getCompositeList( );
    }

    @Override
    public ICompositeDisplay templateDisplayToComposite( FormDisplay templateDisplay, int nIterationNumber )
    {
        ICompositeDisplay composite = null;
        if ( FormsConstants.COMPOSITE_GROUP_TYPE.equals( templateDisplay.getCompositeType( ) ) )
        {
            composite = new CompositeTemplateGroupDisplay( templateDisplay, nIterationNumber );

        }
        else
            if ( FormsConstants.COMPOSITE_QUESTION_TYPE.equals( templateDisplay.getCompositeType( ) ) )
            {
                composite = new CompositeTemplateQuestionDisplay( templateDisplay, nIterationNumber );
            }

        return composite;
    }

    @Override
    public void deleteTemplate( int nIdTemplate )
    {
        IFormDisplayService displayService = SpringContextService.getBean( TemplateDisplayService.BEAN_NAME );

        List<FormDisplay> listChildrenDisplay = TemplateDisplayHome.getFormDisplayListByParent( nIdTemplate, DISPLAY_ROOT_PARENT_ID );

        for ( FormDisplay childDisplay : listChildrenDisplay )
        {
            displayService.deleteDisplayAndDescendants( childDisplay.getId( ) );
        }

        TemplateStepHome.remove( nIdTemplate );
    }
}
