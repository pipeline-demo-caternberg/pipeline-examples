// vars/defineProps.groovy
def call() {
  try {
    java.net.InetAddress.getLocalHost().getHostName();
  } catch (UnknownHostException e) {

  }
}
