package org.ballerinalang.langlib.xml;

import io.ballerina.runtime.api.values.BXml;
import org.apache.axiom.om.OMComment;
import org.apache.axiom.om.OMProcessingInstruction;

/**
 * Helper functions to manipulate OM elements.
 */
public class XMLValueUtil {

    /**
     * Get target potion of xml processing instruction.
     *
     * @param xmlValue xml processing instruction.
     * @return target.
     */
    public static String getTarget(BXml xmlValue) {
       return ((OMProcessingInstruction) xmlValue.value()).getTarget();
    }

    /**
     * Get content of processing instruction.
     *
     * @param value xml value.
     * @return processing instruction content.
     */
    public static String getPIContent(BXml value) {
        return ((OMProcessingInstruction) value.value()).getValue();
    }

    /**
     * Get value of XML comment.
     *
     * @param value xml value.
     * @return comment content.
     */
    public static String getCommentContent(BXml value) {
        return ((OMComment) value.value()).getValue();
    }
}
