from pygments.lexer import RegexLexer
from pygments.token import Punctuation, Keyword, Name, String, Comment, Operator, Text
from pygments.util import shebang_matches

class CourierLexer(RegexLexer):
  name = 'courier'
  aliases = ['courier']
  filenames = ['*.courier']
  tokens = {
    'root': [
      (r'[^\S\n]+', Text),
      (r'//.*?\n', Comment.Single),
      (r'/\*.*?\*/', Comment.Multiline),
      (r'"(\\\\|\\"|[^"])*"', String),
      (r'\b(namespace|import|record|enum|fixed|typeref|union|map|array|nil|null|true|false)\s*\b', Keyword),
      (r'[A-Za-z][A-Za-z0-9_]*', Name.Label),
      (r'[\[\]\(\)\{\}=:,.?@]', Operator)
    ],
  }
  def analyse_text(text):
    return shebang_matches(text, r'courier')
