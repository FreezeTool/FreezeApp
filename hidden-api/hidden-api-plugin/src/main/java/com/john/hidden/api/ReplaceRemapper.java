package com.john.hidden.api;

import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;

import org.objectweb.asm.commons.Remapper;


class ReplaceRemapper extends Remapper {
    private final ClassContext context;

    public ReplaceRemapper(final ClassContext context) {
        this.context = context;
    }

    @Override
    public String map(final String typeName) {
        final ClassData data = context.loadClassData(typeName.replace('/', '.') + "$" + Constants.REFINE_METADATA_CLASS_NAME);
        if (data == null) {
            return typeName;
        }

        if (data.getClassAnnotations().contains(Descriptor.class.getName())) {
            final String to = data.getClassAnnotations().stream()
                    .filter(a -> a.startsWith(Constants.REFINE_NS_PACKAGE))
                    .findFirst()
                    .orElseThrow(() -> new UnsupportedOperationException("Use deprecated refine class " + data.getClassName()));

            return to.substring(Constants.REFINE_NS_PACKAGE.length() + 1).replace('.', '/');
        }

        return typeName;
    }

    @Override
    public String mapInnerClassName(String name, String ownerName, String innerName) {
        final String result = super.mapInnerClassName(name, ownerName, innerName);

        if (innerName.startsWith("$") && !result.startsWith("$")) {
            return "$" + result;
        }

        return result;
    }
}