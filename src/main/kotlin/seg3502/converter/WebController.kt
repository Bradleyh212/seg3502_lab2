package seg3502.converter

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class WebController {
    @ModelAttribute
    fun addAttributes(model: Model) {
        model.addAttribute("error", "")
        model.addAttribute("celsius", "")
        model.addAttribute("fahrenheit", "")
        model.addAttribute("calculationError", "")
        model.addAttribute("firstNumber", "")
        model.addAttribute("secondNumber", "")
        model.addAttribute("calculatorResult", "")
        model.addAttribute("calculatorOperation", "")
    }

    @RequestMapping("/")
    fun home(): String {
        return "home"
    }

    @GetMapping(value = ["/convert"])
    fun doConvert(
        @RequestParam(value = "celsius", required = false) celsius: String,
        @RequestParam(value = "fahrenheit", required = false) fahrenheit: String,
        @RequestParam(value = "operation", required = false) operation: String,
        model: Model
    ): String {
        var celsiusVal: Double
        var fahrenheitVal: Double
        when (operation) {
            "CtoF" ->
                try {
                    celsiusVal = celsius.toDouble()
                    fahrenheitVal = ((celsiusVal * 9) / 5 + 32)
                    model.addAttribute("celsius", celsius)
                    model.addAttribute("fahrenheit", String.format("%.2f", fahrenheitVal))
                } catch (exp: NumberFormatException) {
                    model.addAttribute("error", "CelsiusFormatError")
                    model.addAttribute("celsius", celsius)
                    model.addAttribute("fahrenheit", fahrenheit)
                }
            "FtoC" ->
                try {
                    fahrenheitVal = fahrenheit.toDouble()
                    celsiusVal = ((fahrenheitVal - 32) * 5) / 9
                    model.addAttribute("celsius", String.format("%.2f", celsiusVal))
                    model.addAttribute("fahrenheit", fahrenheit)
                } catch (exp: NumberFormatException) {
                    model.addAttribute("error", "FahrenheitFormatError")
                    model.addAttribute("celsius", celsius)
                    model.addAttribute("fahrenheit", fahrenheit)
                }
            else -> {
                model.addAttribute("error", "OperationFormatError")
                model.addAttribute("celsius", celsius)
                model.addAttribute("fahrenheit", fahrenheit)
            }
        }
        return "home"
    }

    @GetMapping(value = ["/calculate"])
    fun calculate(
        @RequestParam(value = "firstNumber", required = false) firstNumber: String,
        @RequestParam(value = "secondNumber", required = false) secondNumber: String,
        @RequestParam(value = "calculatorOperation", required = false) calculatorOperation: String,
        model: Model
    ): String {
        model.addAttribute("firstNumber", firstNumber)
        model.addAttribute("secondNumber", secondNumber)
        model.addAttribute("calculatorOperation", calculatorOperation)

        try {
            val firstValue = firstNumber.toDouble()
            val secondValue = secondNumber.toDouble()
            val result = when (calculatorOperation) {
                "add" -> firstValue + secondValue
                "subtract" -> firstValue - secondValue
                "multiply" -> firstValue * secondValue
                "divide" -> {
                    if (secondValue == 0.0) {
                        model.addAttribute("calculationError", "DivisionByZeroError")
                        return "home"
                    }
                    firstValue / secondValue
                }
                else -> {
                    model.addAttribute("calculationError", "CalculatorOperationError")
                    return "home"
                }
            }
            model.addAttribute("calculatorResult", String.format("%.2f", result))
        } catch (exp: NumberFormatException) {
            model.addAttribute("calculationError", "CalculatorFormatError")
        }

        return "home"
    }
}