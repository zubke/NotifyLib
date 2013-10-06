NotifyLib
=========

WORK IN PROGRESS

NotifyLib is a Android UI-component library containing tools to manage notifies.
A notify is a small message to inform the end-user about the state of an application. For example an orange Notify could indicate a warning.
The NotifyManager class helps you create and show these.

<h3>Code examples</h3>

<pre><code>This is a sample code block.
</code></pre>

<h3>Limitations</h3>
Implementation of this library uses the ViewAnimator class to show Notifies. All views inflated into the NotifyManger should use the same height else the height of the NotifyFlipper will be equal to the height of the largest view. Future updates will handle the migration to an own created ViewAnimator.
