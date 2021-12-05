(function () {
	YAHOO.Bubbling.fire("registerAction", {
		actionName: "onToPDF",
		fn: function toPDF(file) {

			var docActions = this;
			var onConfirmActionConfig = {
				success: {
					callback: {
						fn: function toPDFSuccess(response) {
							response.json = JSON.parse(response.serverResponse.responseText);
							var title = response.json.success === true ? "Sukces" : "Błąd";
              var responseMsg = response.json.msg || title;
							Alfresco.util.PopupManager.displayPrompt({
                title: title,
                text: responseMsg
              });
						},
						scope: this
					}
				},
				failure: {
					message: this.msg("Błąd", file.displayName, Alfresco.constants.USERNAME)
				},
				webscript: {
					name: "/api/patom/action/toPDF",
					stem: Alfresco.constants.PROXY_URI,
					method: Alfresco.util.Ajax.POST
				},
				config: {
				    requestContentType : Alfresco.util.Ajax.JSON,
				    dataObj: {
				        documentNodeRef: file.nodeRef
				    }
				}
			}

			var buttons =
				[{
					text: this.msg("button.yes"),
					handler: function onConfirmAction() {
						docActions.modules.actions.genericAction(onConfirmActionConfig);
						this.destroy();
					}
				}, {
					text: this.msg("button.no"),
					handler: function onCancelAction() {
						this.destroy();
					},
					isDefault: true
				}
			];

			Alfresco.util.PopupManager.displayPrompt({
				title: this.msg("label.action.toPDF.prompt.title"),
				text: this.msg("label.action.toPDF.prompt.text"),
				noEscape: true,
				buttons: buttons
			});

		}
	});

})();
