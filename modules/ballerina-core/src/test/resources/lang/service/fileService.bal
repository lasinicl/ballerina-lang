@Source (
protocol = "file",
fileURI = "file:///home/desktop/test.txt",
pollingInterval = "1000")
service fileServiceWithoutResource {
}

@Source (
protocol = "file",
fileURI = "file:///home/desktop/test.txt",
pollingInterval = "1000")
service fileServiceWithResource {
    @OnFile
    resource fileResource(message m) {
    }
}
