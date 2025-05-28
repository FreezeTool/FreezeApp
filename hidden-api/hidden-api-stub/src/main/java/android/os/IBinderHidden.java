package android.os;


import java.io.FileDescriptor;

public interface IBinderHidden {
    public void shellCommand(FileDescriptor in, FileDescriptor out,
                             FileDescriptor err,
                             String[] args, ShellCallback shellCallback,
                             ResultReceiver resultReceiver) throws RemoteException;
}
