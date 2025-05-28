package com.john.hidden.api;

import com.android.build.api.instrumentation.AsmClassVisitorFactory;
import com.android.build.api.instrumentation.ClassContext;
import com.android.build.api.instrumentation.ClassData;
import com.android.build.api.instrumentation.InstrumentationParameters;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;

import javax.annotation.Nonnull;

/**
 * Factory to create classes refine visitor.
 */
public abstract class ReplaceFactory implements AsmClassVisitorFactory<InstrumentationParameters> {
    @Override
    @Nonnull
    public ClassVisitor createClassVisitor(final @Nonnull ClassContext classContext, final @Nonnull ClassVisitor classVisitor) {
        return new ClassRemapper(getInstrumentationContext().getApiVersion().get(), classVisitor, new ReplaceRemapper(classContext)) {};
    }

    @Override
    public boolean isInstrumentable(final @Nonnull ClassData classData) {
        return true;
    }
}